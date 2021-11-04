package com.visk.android.stockmanager.repository

import android.util.Log
import com.visk.android.stockmanager.Constant
import com.visk.android.stockmanager.db.entity.StockInfo
import com.visk.android.stockmanager.db.dao.StockDao
import com.visk.android.stockmanager.db.entity.StockMine
import com.visk.android.stockmanager.db.entity.StockTrade
import com.visk.android.stockmanager.stock.StockInfoDTO
import com.visk.android.stockmanager.stock.StockRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@FlowPreview
@Singleton
class StockRepository  @Inject constructor(val remoteDataSource : StockRemoteDataSource, val stockDao: StockDao ) {
    fun getStockListFlow() = stockDao.getStockInfoFlow().distinctUntilChanged()
    fun myStockListFlow() =
        stockDao.getMyStockIdFlow().distinctUntilChanged().flatMapConcat {
            stockDao.getStockInfoFlow(it).distinctUntilChanged()
        }


    fun getMyStockFlow() = stockDao.getMyStockFlow().distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun requestStockInfo() : List<StockInfo>{
        return withContext(Dispatchers.IO)
        {
             requestStock(stockDao.getStockIds())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun requestMyStockInfo() {
        withContext(Dispatchers.IO)
        {
            requestStock(stockDao.getMyStockIds())
        }
    }
    private suspend fun requestStock(ids: List<String>): List<StockInfo> {
        return kotlin.runCatching {
            val response = ids.asFlow().flatMapMerge {
                flow {
                    val response = remoteDataSource.getStockInfo(it)
                    emit(response)
                }
            }.toList()
            val list = response.map { it.mapStock() }
            stockDao.insertStock(list)
            list
        }.getOrElse { emptyList() }
    }

    suspend fun addStock(stockId: String) {
        val response = remoteDataSource.getStockInfo(stockId)
        stockDao.insertStock(response.mapStock())
    }

    suspend fun getStockCode(name : String) = remoteDataSource.getStockCode(name)


    suspend fun addTrade(name: String, price: Int, volume: Int, date: String , type : Int) {
        val stockId = stockDao.getStockId(name.trim())
        stockDao.insertTrade(StockTrade(stockId, price, volume, date, type))

        val stockMine = stockDao.getStockMine(stockId)?.apply {
            when (type) {
                Constant.Trade.BUY -> {
                    totalPrice = totalPrice + price * volume
                    this.volumn = volumn + volume
                }
                Constant.Trade.SELL -> {
                    val average = totalPrice / volumn
                    this.volumn = volumn - volume
                    totalPrice = average * volume
                }
            }

        } ?: StockMine(stockId, volume, price * volume, date)

        if (stockMine.volumn == 0) {
            stockDao.deleteMyStock(StockMine(stockId = stockId))
        } else {
            stockDao.insertMyStock(stockMine)
        }

    }


    private fun StockInfoDTO.mapStock() = StockInfo(
        result.areas.get(0).datas.get(0).stockId,
        result.areas.get(0).datas.get(0).name,
        result.areas.get(0).datas.get(0).currentPrice,
        result.areas.get(0).datas.get(0).yesterdayPrice,
        result.areas.get(0).datas.get(0).tradeVolume,
        result.areas.get(0).datas.get(0).diffPercent,
        result.areas.get(0).datas.get(0).todayHigh,
        result.areas.get(0).datas.get(0).todayLow,
        SimpleDateFormat("hh:mm").format(Calendar.getInstance().time)
    )
}