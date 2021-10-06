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


    fun myStocksLive() = stockRepository.myStockListFlow() // Todo fix flexible
        .combine(stockRepository.getMyStockFlow()) { stockInfo: List<StockInfo>, stockMine: List<StockMine> ->
            stockMine.mapNotNull {
                val id = it.stockId
                stockInfo.find { id == it.stockId }?.run {
                    MyStock(
                        name,
                        currentPrice,
                        it.totalPrice / it.volumn,
                        it.volumn,
                        it.totalPrice / it.volumn - currentPrice,
                        it.totalPrice / it.volumn / currentPrice * 100
                    )
                }
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