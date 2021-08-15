package com.visk.android.stockmanager.repository

import android.text.format.DateFormat
import android.util.Log
import com.visk.android.stockmanager.domain.Stock
import com.visk.android.stockmanager.stock.StockInfoDTO
import com.visk.android.stockmanager.stock.StockRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.internal.ChannelFlow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@FlowPreview
class StockRepository   {
    val remoteDataSource = StockRemoteDataSource()

    private val stockChannel = ConflatedBroadcastChannel<List<Stock>>()

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun requestStockInfo(stockIds: List<String>): List<Stock> {
        return withContext(Dispatchers.IO)
        {

            val resultList = stockIds.asFlow().flatMapMerge {
                flow {
                    val result = remoteDataSource.getStockInfo(it).map()
                    emit(result)
                }
            }.toList().sortedBy { it.name }
            stockChannel.send(resultList)
            return@withContext resultList
        }
    }

    fun getStockListFlow() = stockChannel.asFlow()

    private fun StockInfoDTO.map() = Stock(
        result.areas.get(0).datas.get(0).nm,
        result.areas.get(0).datas.get(0).nv,
        result.areas.get(0).datas.get(0).sv,
        result.areas.get(0).datas.get(0).aq,
        result.areas.get(0).datas.get(0).cr,
        SimpleDateFormat("hh:mm").format(Calendar.getInstance().time)

    )

}