package com.visk.android.stockmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.visk.android.stockmanager.repository.StockRepository
import kotlinx.coroutines.launch

class StockViewModel : ViewModel() {

    val stockRepository = StockRepository()
    val stockList = listOf("005930", "027740", "068270", "032350")
    val stockLiveData = stockRepository.getStockListFlow().asLiveData()
    fun getStockInfo() {
        viewModelScope.launch {
            stockRepository.requestStockInfo(stockList)
        }
    }
}