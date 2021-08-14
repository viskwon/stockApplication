package com.visk.android.stockmanager.domain

data class Stock(val name : String , val currentPrice :Int , val yesterdayPrice : Int , val tradeVolume : Int , val diffPercent : Number)