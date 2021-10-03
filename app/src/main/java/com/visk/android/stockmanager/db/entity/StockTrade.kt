package com.visk.android.stockmanager.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class StockTrade(var stockId: String, var volumn: Int, var price: Int, var date: Long) {
    @PrimaryKey(autoGenerate = true)
    var tradeId: Int = 0
}