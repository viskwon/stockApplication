package com.visk.android.stockmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.visk.android.stockmanager.StockUsecase
import com.visk.android.stockmanager.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DynamicStockViewModel @Inject constructor(
    stockUsecase: StockUsecase,
    application: Application
) : AndroidViewModel(application) {

    val stockProfitList = stockUsecase.stockHistoryProfit().asLiveData()


}