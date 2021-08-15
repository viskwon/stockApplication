package com.visk.android.stockmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import com.visk.android.stockmanager.repository.StockRepository
import com.visk.android.stockmanager.worker.StockRefreshWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class StockViewModel(application: Application) : AndroidViewModel(application) {

    private val stockRepository = StockRepository.getInstance()
    private val stockList = listOf("005930", "027740", "068270", "032350")
    val stockLiveData = stockRepository.getStockListFlow().asLiveData()
    fun getStockInfo() {
        viewModelScope.launch {
            stockRepository.requestStockInfo(stockList)
        }
    }

    fun autoRefresh(){
        viewModelScope.launch {
            while (true){
                stockRepository.requestStockInfo(stockList)
                delay(5000)
            }
        }
    }


}