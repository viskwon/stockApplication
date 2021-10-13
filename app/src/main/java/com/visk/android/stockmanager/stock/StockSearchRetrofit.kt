package com.visk.android.stockmanager.stock

import retrofit2.http.GET
import retrofit2.http.Query

interface StockSearchRetrofit {

    @GET("search.naver")
    suspend fun getStockCode(@Query("query") name: String) : String


}