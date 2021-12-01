package com.visk.android.stockmanager

import android.util.Log
import com.visk.android.stockmanager.Constant.Trade.BUY
import com.visk.android.stockmanager.Constant.Trade.SELL
import com.visk.android.stockmanager.db.entity.StockInfo
import com.visk.android.stockmanager.db.entity.StockMine
import com.visk.android.stockmanager.db.entity.StockTrade
import com.visk.android.stockmanager.domain.CombineStock
import com.visk.android.stockmanager.repository.StockRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StockUsecase @Inject constructor(
    val stockRepository: StockRepository
) {

    fun getCombineStock() = stockRepository.tradeListFlow()
        .combine(stockRepository.myStockInfoListFlow()) { stockTrade: List<StockTrade>, stockInfo: List<StockInfo> ->
            Log.d("hjskwon","getCombineStock ${stockTrade.size} ${stockInfo.size}")
            val list = mutableListOf<CombineStock>()
            var combineStock = CombineStock()
            stockTrade.forEach {
                if (it.stockId == combineStock.id) {
                    it.fillCombind(combineStock)
                } else {
                    if (combineStock.id.isNotBlank()) {
                        Log.d("hjskwon","add")
                        list.add(combineStock)
                        combineStock = CombineStock()
                    }
                    it.fillCombind(combineStock)
                }
            }
            if (combineStock.id.isNotBlank()) {
                Log.d("hjskwon","add 2")
                list.add(combineStock)
            }
            for (info in stockInfo) {
                list.find { it.id == info.stockId }?.apply {
                    profit += remain * info.currentPrice
                    name = info.name
                }
            }
Log.d("hjskwon","list ${list.size}")
            list.forEach {
                if(it.name.isBlank()){
                    stockRepository.getStockName(it.id)?.run { it.name = this }
                }
            }
            list.toList()

        }




    fun StockTrade.fillCombind(combineStock: CombineStock){
        combineStock.id = stockId
        when (type) {
            BUY -> {
                combineStock.remain += volumn
                combineStock.profit -= price * volumn
            }
            SELL -> {
                combineStock.remain -= volumn
                combineStock.profit += price * volumn
            }
        }
    }

}

/*
* map {
            val list = mutableListOf<CombineStock>()
            var combineStock = CombineStock()
            it.forEach {
                if (it.stockId == combineStock.id) {
                    it.fillCombind(combineStock)
                } else {
                    if (combineStock.id.isNotBlank()) {
                        list.add(combineStock)
                        combineStock = CombineStock()
                    }
                    it.fillCombind(combineStock)
                }
            }
            if(combineStock.id.isNotBlank()){
                list.add(combineStock)
            }

            list.toList()
        }
* */