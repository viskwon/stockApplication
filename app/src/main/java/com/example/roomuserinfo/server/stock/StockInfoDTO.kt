package com.example.roomuserinfo.server.stock

data class StockInfoDTO(val resultCode : String , val result: StockResult)

data class StockResult(val pollingInterval :Int , val areas: List<StockArea>)

data class StockArea(val datas: List<StockDatas>)

data class StockDatas(val nm:String,val nv :Int ,val sv :Int , val aq :Int , val cr : Float)