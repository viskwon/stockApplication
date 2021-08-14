package com.example.stockmanager.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmanager.domain.Stock
import com.example.stockmanager.repository.StockRepository
import kotlinx.coroutines.launch

class StockViewModel () : ViewModel(){

    val stockList = listOf<String>("005930", "027740", "068270", "032350")
    private val _stockListLiveData = MutableLiveData<List<Stock>>()
    val stockLiveData: LiveData<List<Stock>>
        get() = _stockListLiveData

    fun getStockInfo() {
        viewModelScope.launch {
            Log.d("hjswon","getStockInfo")

            val stockList = StockRepository().requestStockInfo(stockList)
            Log.d("hjswon","getStockInfo come" +stockList.size)

            _stockListLiveData.value = stockList
        }
    }
}