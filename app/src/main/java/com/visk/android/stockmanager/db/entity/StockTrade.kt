package com.visk.android.stockmanager.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class StockTrade(var stockId: String, var price: Int,var volumn: Int,  var date: String) {
    @PrimaryKey(autoGenerate = true)
    var tradeId: Int = 0
}