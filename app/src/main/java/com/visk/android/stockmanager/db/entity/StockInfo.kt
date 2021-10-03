package com.visk.android.stockmanager.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class StockInfo(
    @PrimaryKey
    val stockId: String,
    val name: String,
    val currentPrice: Int,
    val yesterdayPrice: Int,
    val tradeVolume: Int,
    val diffPercent: Float,
    val updateTime: String
)