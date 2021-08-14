package com.example.stockmanager.server.stock

import retrofit2.http.GET
import retrofit2.http.Query

interface StockRetrofit {

    @GET("api/realtime")
    suspend fun getStockInfo(@Query("query") code: String) : StockInfoDTO



}