package com.visk.android.stockmanager

import com.visk.android.stockmanager.Constant.Trade.BUY
import com.visk.android.stockmanager.Constant.Trade.SELL
import com.visk.android.stockmanager.db.entity.StockTrade
import com.visk.android.stockmanager.domain.ProfitStock
import com.visk.android.stockmanager.repository.StockRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StockUsecase @Inject constructor(
    val stockRepository: StockRepository
) {

    fun stockHistoryProfit() = stockRepository.myStockInfoListFlow().map {
        val profitStockList = getProfitList()

        for (stockInfo in it) {
            profitStockList.find { it.id == stockInfo.stockId }?.apply {
                profit += remain * stockInfo.currentPrice
                name = stockInfo.name
            }
        }

        profitStockList.forEach {
            if (it.name.isBlank()) {
                stockRepository.getStockName(it.id)?.run { it.name = this }
            }
        }
        profitStockList.toList()
    }

    private suspend fun getProfitList() = mutableListOf<ProfitStock>().apply {
        var profitStock = ProfitStock()
        stockRepository.getTradeList().forEach {
            if (it.stockId == profitStock.id) {
                it.calculProfit(profitStock)
            } else {
                if (profitStock.id.isNotBlank()) {
                    add(profitStock)
                    profitStock = ProfitStock()
                }
                it.calculProfit(profitStock)
            }
        }
    }

    fun StockTrade.calculProfit(profitStock: ProfitStock) {
        profitStock.id = stockId
        when (type) {
            BUY -> {
                profitStock.remain += volumn
                profitStock.profit -= price * volumn
            }
            SELL -> {
                profitStock.remain -= volumn
                profitStock.profit += price * volumn
            }
        }
    }
}
