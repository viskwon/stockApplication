package com.visk.android.stockmanager.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.visk.android.stockmanager.Constant

@Entity
class StockTrade(
    var stockId: String,
    var price: Int,
    var volumn: Int,
    var date: String,
    @ColumnInfo(defaultValue = "${Constant.Trade.BUY}")
    val type: Int

) {
    @PrimaryKey(autoGenerate = true)
    var tradeId: Int = 0
}