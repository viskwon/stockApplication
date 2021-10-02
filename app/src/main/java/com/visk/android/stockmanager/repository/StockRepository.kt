package com.visk.android.stockmanager.repository

import com.visk.android.stockmanager.db.StockInfo
import com.visk.android.stockmanager.db.dao.StockDao
import com.visk.android.stockmanager.domain.Stock
import com.visk.android.stockmanager.stock.StockInfoDTO
import com.visk.android.stockmanager.stock.StockRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@FlowPreview
class StockRepository(val remoteDataSource : StockRemoteDataSource , val stockDao: StockDao ) {
    fun getStockListFlow() = stockDao.getStockInfoFlow().distinctUntilChanged()

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
        }
    }

    private fun StockInfoDTO.mapStock() = StockInfo(
        result.areas.get(0).datas.get(0).stockId,
        SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().time),
        result.areas.get(0).datas.get(0).name,
        result.areas.get(0).datas.get(0).currentPrice,
        result.areas.get(0).datas.get(0).yesterdayPrice,
        result.areas.get(0).datas.get(0).tradeVolume,
        result.areas.get(0).datas.get(0).diffPercent,
        SimpleDateFormat("hh:mm").format(Calendar.getInstance().time)
    )
}