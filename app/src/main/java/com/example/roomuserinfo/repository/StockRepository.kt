package com.example.roomuserinfo.repository

import android.util.Log
import com.example.roomuserinfo.domain.Stock
import com.example.roomuserinfo.server.stock.StockInfoDTO
import com.example.roomuserinfo.server.stock.StockRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StockRepository   {
    val remoteDataSource = StockRemoteDataSource()
    val channel = Channel<List<Stock>>()

    suspend fun requestStockInfo(stockIds: List<String>): List<Stock> {
        return withContext(Dispatchers.IO)
        {
            Log.d("hjswon", "start")
            val resultList = stockIds.asFlow().flatMapMerge {
                flow {
                    Log.d("hjswon", it)
                    val result = remoteDataSource.getStockInfo(it).map()
                    Log.d("hjswon", "end result")

                    emit(result)
                }
            }.toList()
            Log.d("hjswon", "end" + resultList.size)
            channel.offer(resultList)

            return@withContext resultList
        }

    }


    suspend fun getStockInfoFlow(stockId: String) =
        withContext(Dispatchers.IO) { remoteDataSource.getStockFlow(stockId).map { it.map() } }


    private fun StockInfoDTO.map() = Stock(
        result.areas.get(0).datas.get(0).nm,
        result.areas.get(0).datas.get(0).nv,
        result.areas.get(0).datas.get(0).sv,
        result.areas.get(0).datas.get(0).aq,
        result.areas.get(0).datas.get(0).cr
    )

}