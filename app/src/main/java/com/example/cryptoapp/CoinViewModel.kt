package com.example.cryptoapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cryptoapp.database.AppDatabase
import com.example.cryptoapp.pojo.ApiFactory
import com.example.cryptoapp.pojo.CoinPriceInfo
import com.example.cryptoapp.pojo.CoinPriceInfoRawData
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class CoinViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()
    val priceList = db.coinPriceInfoDao().getPriceList()
    init {
        loadData()
    }

    fun getDetailInfo(fSym: String):LiveData<CoinPriceInfo> {
        return db.coinPriceInfoDao().getPriceInfoAboutCoin(fSym)
    }

    fun loadData() {
        val disposable = ApiFactory.apiService.getTopCoinsInfo(limit = 5)
            .map { it.data?.map { it.coinInfo?.name }?.joinToString(",") }
            .flatMap { ApiFactory.apiService.getFullPriceList(fSyms = it) }
            .map { getPriceListFromRawData(it) }
            .delaySubscription(10,TimeUnit.SECONDS)
            .repeat() //бесконечная загрузка (т.е.повторное віполнение) Если ошибка во время загрузки то возобновлятся повтор не будет
            .retry() //выполнит загрузку если произошла ошибка
            .subscribeOn(io.reactivex.schedulers.Schedulers.io()) //делаем в другом потоке

            //   .observeOn(AndroidSchedulers.mainThread()) //переключение на главный поток
            .subscribe(
                {
                    //  val a=it.data?.map { it.coinInfo?.name}?.joinToString ("," )
                    Log.d("TEST_OF_LOADING_DATA", "Success: $it.toString()")
                    db.coinPriceInfoDao().insertPriceList(it)
                },
                {
                    Log.d("TEST_OF_LOADING_DATA", "Failure: $it.message.toString()")
                }
            )

        compositeDisposable.add(disposable)
    }

    private fun getPriceListFromRawData(
        coinPriceInfoRawData: CoinPriceInfoRawData
    ): List<CoinPriceInfo> {
        val result=ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRawData.coinPriceInfoJsonObject ?: return result //равносильно if (jsonObject == null) return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinPriceInfo::class.java
                )
                result.add(priceInfo)
            }

        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}