package com.visk.android.stockmanager.stock

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

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
    private val retrofitSearch = Retrofit.Builder()
        .baseUrl("https://search.naver.com/")
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build().create(StockSearchRetrofit::class.java)


    suspend fun getStockInfo(stockId: String) =
        retrofit.getStockInfo("SERVICE_ITEM:" + stockId)
    suspend fun getStockCode(name: String) =
        retrofitSearch.getStockCode(name).let {
            val keyword = "http://finance.naver.com/item/main.nhn?code="
            val startindex = it.indexOf(keyword)
            it.substring(startindex +keyword.length ,startindex+6+keyword.length)
        }


}
