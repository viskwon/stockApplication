package com.visk.android.stockmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.visk.android.stockmanager.StockApplication
import com.visk.android.stockmanager.db.entity.StockInfo
import com.visk.android.stockmanager.db.entity.StockMine
import com.visk.android.stockmanager.domain.MyStock
import com.visk.android.stockmanager.repository.StockRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MyStockViewModel (application: Application) : AndroidViewModel(application){
    private val stockRepository: StockRepository
        get() {
            return getApplication<StockApplication>().stockRepository
        }


    suspend fun myStocksLive() = stockRepository.myStockListFlow() // Todo fix flexible
        .combine(stockRepository.getMyStockFlow()) { stockInfo: List<StockInfo>, stockMine: List<StockMine> ->
            stockMine.map {
                val id = it.stockId
                val info = stockInfo.find { id == it.stockId }!!
                MyStock(
                    info.name,
                    info.currentPrice,
                    it.price,
                    it.volumn,
                    it.price - info.currentPrice,
                    it.price / info.currentPrice * 100
                )
            }
        }.asLiveData()


    fun autoRefresh(){
        viewModelScope.launch {
            while (true){
                val ids =  stockRepository.getMyStockFlow().first().map { it.stockId }
                stockRepository.requestStockInfo(ids)
                delay(10000)
            }
        }
    }
}