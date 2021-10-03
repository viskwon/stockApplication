package com.visk.android.stockmanager.repository

import com.visk.android.stockmanager.db.entity.StockInfo
import com.visk.android.stockmanager.db.dao.StockDao
import com.visk.android.stockmanager.stock.StockInfoDTO
import com.visk.android.stockmanager.stock.StockRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class StockRepository(val remoteDataSource : StockRemoteDataSource , val stockDao: StockDao ) {
    fun getStockListFlow() = stockDao.getStockInfoFlow().distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun requestStockInfo(stockIds: List<String>) {
       withContext(Dispatchers.IO)
       {
           val resultList = stockDao.getStockIds().asFlow().flatMapMerge {
               flow {
                   val response = remoteDataSource.getStockInfo(it)
                   emit(response)
               }
           }.toList()
           stockDao.insertStock(resultList.map { it.mapStock() })
       }
    }

    suspend fun addStock(stockId: String) {
        val response = remoteDataSource.getStockInfo(stockId)
        stockDao.insertStock(response.mapStock())
    }

    private fun StockInfoDTO.mapStock() = StockInfo(
        result.areas.get(0).datas.get(0).stockId,
        result.areas.get(0).datas.get(0).name,
        result.areas.get(0).datas.get(0).currentPrice,
        result.areas.get(0).datas.get(0).yesterdayPrice,
        result.areas.get(0).datas.get(0).tradeVolume,
        result.areas.get(0).datas.get(0).diffPercent,
        SimpleDateFormat("hh:mm").format(Calendar.getInstance().time)
    )
}