package com.visk.android.stockmanager.stock

import retrofit2.http.GET
import retrofit2.http.Query

interface StockRetrofit {

    @GET("api/realtime")
    suspend fun getStockInfo(@Query("query") code: String) : StockInfoDTO



}