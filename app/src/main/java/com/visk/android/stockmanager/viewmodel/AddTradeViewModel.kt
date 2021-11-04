package com.visk.android.stockmanager.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.visk.android.stockmanager.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddTradeViewModel @Inject constructor(
    val stockRepository: StockRepository,
    application: Application
) : AndroidViewModel(application) {

    suspend fun addTrade(stockId: String, price: String, volume: String, date: Long, type: Int) {

        Log.d("hjskwon", "$stockId , $price ,$volume $type")
        stockRepository.addTrade(
            stockId,
            price.trim().toInt(),
            volume.trim().toInt(),
            SimpleDateFormat("yyyymmdd").format(Date(date)),
            type
        )
    }
}