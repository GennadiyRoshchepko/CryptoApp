package com.example.cryptoapp.pojo

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top/totalvolfull")
    fun getTopCoinsInfo(
        @Query(QUERY_PARAM_API_KEY_) apiKey: String="6e69ab42b1aecad522a5b361ea6e229d86e51349d55f11020c45cebcd0c9140d",
        @Query(QUERY_PARAM_LIMIT) limit: Int=5,
        @Query(QUERY_PARAM_TO_SYMBOL) tSym: String=CURRENCY

    ): Single<CoinInfoListOFDate>
    @GET("pricemultifull")
    fun getFullPriceList(
        @Query(QUERY_PARAM_API_KEY_) apiKey: String="6e69ab42b1aecad522a5b361ea6e229d86e51349d55f11020c45cebcd0c9140d",
        @Query(QUERY_PARAM_FROM_SYMBOLS) fSyms: String,
        @Query(QUERY_PARAM_TO_SYMBOLS) tSyms: String=CURRENCY

    ): Single<CoinPriceInfoRawData>
    companion object {
        private const val QUERY_PARAM_LIMIT="limit"
        private const val QUERY_PARAM_TO_SYMBOL="tsym"
        private const val QUERY_PARAM_API_KEY_="api_key"
        private const val CURRENCY="USD"
        private const val QUERY_PARAM_FROM_SYMBOLS="fsyms"
        private const val QUERY_PARAM_TO_SYMBOLS="tsyms"


    }
}