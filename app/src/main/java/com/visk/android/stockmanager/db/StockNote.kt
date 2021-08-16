package com.visk.android.stockmanager.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class StockNote(
    var stockId: String,
    val note: String
) {
    @PrimaryKey(autoGenerate = true)
    var noteId: Int = 0
}