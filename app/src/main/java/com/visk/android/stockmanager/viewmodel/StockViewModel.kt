package com.visk.android.stockmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.visk.android.stockmanager.StockApplication
import com.visk.android.stockmanager.db.entity.StockInfo
import com.visk.android.stockmanager.domain.Stock
import com.visk.android.stockmanager.repository.StockRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class StockViewModel(application: Application ) : AndroidViewModel(application) {

    private val stockRepository: StockRepository
        get() {
           return getApplication<StockApplication>().stockRepository
        }


    val stockLiveData =
        stockRepository.getStockListFlow().map { it.map { it.toStock() }.sortedBy { it.name } }
            .asLiveData()
    fun getStockInfo() {
        viewModelScope.launch {
            stockRepository.requestStockInfo()
        }
    }

    fun autoRefresh(){
        viewModelScope.launch {
            while (true){
                stockRepository.requestStockInfo()
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
        todayHigh,
        todayLow,
        updateTime
    )

    fun addStock (input : String){
        viewModelScope.launch {
            val stockId = input.toIntOrNull()?.run {
                toString()
            } ?: stockRepository.getStockCode(input)

            stockRepository.addStock(stockId)
        }
    }
}