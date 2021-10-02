package com.visk.android.stockmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import com.visk.android.stockmanager.StockApplication
import com.visk.android.stockmanager.db.StockInfo
import com.visk.android.stockmanager.domain.Stock
import com.visk.android.stockmanager.repository.StockRepository
import com.visk.android.stockmanager.worker.StockRefreshWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class StockViewModel(application: Application ) : AndroidViewModel(application) {

    private val stockRepository: StockRepository
        get() {
           return getApplication<StockApplication>().stockRepository
        }


    private val stockList = listOf("005930", "027740", "068270", "032350")
    val stockLiveData =
        stockRepository.getStockListFlow().map { it.map { it.toStock() }.sortedBy { it.name } }
            .asLiveData()
    fun getStockInfo() {
        viewModelScope.launch {
            stockRepository.requestStockInfo(stockList)
        }
    }

    fun autoRefresh(){
        viewModelScope.launch {
            while (true){
                stockRepository.requestStockInfo(stockList)
                delay(60000)
            }
        }
    }

    fun StockInfo.toStock() = Stock(
        name,
        currentPrice,
        yesterdayPrice,
        tradeVolume,
        diffPercent,
        updateTime
    )
}