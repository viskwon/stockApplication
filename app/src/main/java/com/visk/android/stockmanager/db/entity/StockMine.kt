package com.visk.android.stockmanager.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class StockMine(
    @PrimaryKey var stockId: String,
    var volumn: Int,
    var price: Int,
    var startDate: String
)