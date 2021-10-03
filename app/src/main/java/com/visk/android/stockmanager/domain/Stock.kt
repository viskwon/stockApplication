package com.visk.android.stockmanager.domain

data class Stock(
    val name: String,
    val currentPrice: Int,
    val yesterdayPrice: Int,
    val tradeVolume: Int,
    val diffPercent: Number,
    val todayHigh: Int,
    val todayLow: Int,
    val updateTime: String
)