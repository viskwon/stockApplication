package com.visk.android.stockmanager.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = arrayOf("stockId","date"))
class StockInfo(
    val stockId: String,
    val date: String,
    val name: String,
    val currentPrice: Int,
    val yesterdayPrice: Int,
    val tradeVolume: Int,
    val diffPercent: Float,
    val updateTime: String
)