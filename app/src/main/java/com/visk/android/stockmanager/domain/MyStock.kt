package com.visk.android.stockmanager.domain

data class MyStock(
    val name: String,
    val currentPrice: Int,
    val buyPrice:Int,
    val volume: Int,
    val diff:Int,
    val diffPercent: Number
)