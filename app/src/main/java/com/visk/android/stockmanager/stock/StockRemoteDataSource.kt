package com.visk.android.stockmanager.stock

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StockRemoteDataSource {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://polling.finance.naver.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(StockRetrofit::class.java)

    suspend fun getStockInfo(stockId: String) =
        retrofit.getStockInfo("SERVICE_ITEM:" + stockId)

    suspend fun getStockFlow(stockId: String) =
        flow { emit(retrofit.getStockInfo("SERVICE_ITEM:" + stockId)) }
}
