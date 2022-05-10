package com.example.cryptoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.adapters.CoinInfoAdapter
import com.example.cryptoapp.pojo.CoinPriceInfo
import kotlinx.android.synthetic.main.activity_coin_price_list.*


class CoinPriceListActivity : AppCompatActivity() {
   // private val compositeDisposable = CompositeDisposable()
    private lateinit var viewModel:CoinViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_price_list)
        val adapter = CoinInfoAdapter(this)
        adapter.onCoinClickListener = object : CoinInfoAdapter.OnCoinClickListener {
            override fun onCoinClick(coinPriceInfo: CoinPriceInfo) {
              val intent=CoinDetailActivity.newIntent(this@CoinPriceListActivity,coinPriceInfo.fromsymbol)
                  //Intent(this@CoinPriceListActivity,CoinDetailActivity::class.java)
                //intent.putExtra(CoinDetailActivity.EXTRA_FROM_SYMBOL,coinPriceInfo.fromsymbol)
                startActivity(intent)
            }
        }
        rvCoinPriceList.adapter=adapter
        viewModel = ViewModelProvider(this).get(CoinViewModel::class.java)
       // viewModel = ViewModelProviders.of(this)[CoinViewModel::class.java]
     //   viewModel.loadData() данное действие можно сделать во вью моделе в init
        viewModel.priceList.observe(this, Observer {
        //    Log.d("TEST_OF_LOADING_DATA","Success in activity:$it")
            adapter.coinInfoList=it
        })
      /*  viewModel.getDetailInfo("BTC").observe(this, Observer {
           // Log.d("TEST_OF_LOADING_DATA","Success in activity:$it")
            adapter.coinInfoList=it
        })*/
/*
       val disposable = ApiFactory.apiService.getTopCoinsInfo(tSym = "EOS")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d("TEST_OF_LOADING_DATA",it.toString())
                },
                {
                    Log.d("TEST_OF_LOADING_DATA",it.message.toString())
                }
            )
*/
  /*      val disposable = ApiFactory.apiService.getFullPriceList(fSyms ="ETH")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d("TEST_OF_LOADING_DATA",it.toString())
                },
                {
                    Log.d("TEST_OF_LOADING_DATA",it.message.toString())
                }
            )

        compositeDisposable.add(disposable)
 */   }

    override fun onDestroy() {
        super.onDestroy()
   //     compositeDisposable.dispose()
    }
}