package com.visk.android.stockmanager.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.visk.android.stockmanager.db.entity.StockInfo
import com.visk.android.stockmanager.db.entity.StockNote
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * FROM StockNote ")
    fun getAlphabetizedWords(): List<StockNote>

    @Query("SELECT * FROM StockNote ")
    fun getUserFlow(): Flow<List<StockNote>>

    @Query("SELECT * FROM StockInfo ")
    fun getStockInfoFlow(): Flow<List<StockInfo>>

    @Query("SELECT stockId FROM StockInfo  ")
    suspend fun getStockIds() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stockInfo: List<StockInfo>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stockInfo: StockInfo)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(word: StockNote)

}