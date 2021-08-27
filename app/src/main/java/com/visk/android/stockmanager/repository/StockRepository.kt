package com.visk.android.stockmanager.repository

import com.visk.android.stockmanager.db.StockInfo
import com.visk.android.stockmanager.db.dao.StockDao
import com.visk.android.stockmanager.domain.Stock
import com.visk.android.stockmanager.stock.StockInfoDTO
import com.visk.android.stockmanager.stock.StockRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@FlowPreview
class StockRepository(val remoteDataSource : StockRemoteDataSource , val stockDao: StockDao ) {

    private val stockChannel = ConflatedBroadcastChannel<List<Stock>>()

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun requestStockInfo(stockIds: List<String>) {
       withContext(Dispatchers.IO)
        {

            val resultList = stockIds.asFlow().flatMapMerge {
                flow {
                    val response = remoteDataSource.getStockInfo(it)
                    emit(response)
                }
            }.toList()
            stockDao.insert(resultList.map{it.mapStock()})
            stockChannel.send(resultList.map { it.map() }.sortedBy { it.name })
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

    private fun StockInfoDTO.mapStock() = StockInfo(
        result.areas.get(0).datas.get(0).cd,
        SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().time),
        result.areas.get(0).datas.get(0).nm,
        result.areas.get(0).datas.get(0).nv,
        result.areas.get(0).datas.get(0).sv,
        result.areas.get(0).datas.get(0).aq,
        result.areas.get(0).datas.get(0).cr,
        SimpleDateFormat("hh:mm").format(Calendar.getInstance().time)
    )


}