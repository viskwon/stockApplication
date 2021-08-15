package com.visk.android.stockmanager.repository

import android.text.format.DateFormat
import android.util.Log
import com.visk.android.stockmanager.domain.Stock
import com.visk.android.stockmanager.stock.StockInfoDTO
import com.visk.android.stockmanager.stock.StockRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class StockRepository   {
    val remoteDataSource = StockRemoteDataSource()

    suspend fun requestStockInfo(stockIds: List<String>): List<Stock> {
        return withContext(Dispatchers.IO)
        {
            val resultList = stockIds.asFlow().flatMapMerge {
                flow {
                    val result = remoteDataSource.getStockInfo(it).map()
                    emit(result)
                }
            }.toList().sortedBy { it.name }
            Log.d("hjswon", "end" + resultList.size)

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
        result.areas.get(0).datas.get(0).cr,
        SimpleDateFormat("hh:mm").format(Calendar.getInstance().time)

    )

}