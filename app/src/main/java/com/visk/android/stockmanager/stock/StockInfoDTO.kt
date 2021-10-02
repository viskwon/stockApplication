package com.visk.android.stockmanager.stock

import com.google.gson.annotations.SerializedName

data class StockInfoDTO(val resultCode: String, val result: StockResult)

data class StockResult(val pollingInterval: Int, val areas: List<StockArea>)

data class StockArea(val datas: List<StockDatas>)

data class StockDatas(
    @SerializedName("cd") val stockId: String,
    @SerializedName("nm") val name: String,
    @SerializedName("nv") val currentPrice: Int,
    @SerializedName("sv") val yesterdayPrice: Int,
    @SerializedName("aq") val tradeVolume: Int,
    @SerializedName("cr") val diffPercent: Float
)