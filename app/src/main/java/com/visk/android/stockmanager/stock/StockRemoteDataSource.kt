package com.visk.android.stockmanager.stock

import javax.inject.Inject

class StockRemoteDataSource @Inject constructor(val retrofit: StockRetrofit , val retrofitSearch: StockSearchRetrofit){

    suspend fun getStockInfo(stockId: String) =
        retrofit.getStockInfo("SERVICE_ITEM:" + stockId)
    suspend fun getStockCode(name: String) =
        retrofitSearch.getStockCode(name).let {
            val keyword = "http://finance.naver.com/item/main.nhn?code="
            val startindex = it.indexOf(keyword)
            it.substring(startindex +keyword.length ,startindex+6+keyword.length)
        }
}
