package com.example.roomuserinfo.server.stock

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface StockRetrofit {

    @GET("api/realtime")
    suspend fun getStockInfo(@Query("query") code: String) : StockInfoDTO



}