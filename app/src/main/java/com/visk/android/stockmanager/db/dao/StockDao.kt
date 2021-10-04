package com.visk.android.stockmanager.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.visk.android.stockmanager.db.entity.StockInfo
import com.visk.android.stockmanager.db.entity.StockMine
import com.visk.android.stockmanager.db.entity.StockNote
import com.visk.android.stockmanager.db.entity.StockTrade
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * FROM StockNote ")
    fun getAlphabetizedWords(): List<StockNote>

    @Query("SELECT * FROM StockNote ")
    fun getUserFlow(): Flow<List<StockNote>>

    @Query("SELECT * FROM StockInfo ")
    fun getStockInfoFlow(): Flow<List<StockInfo>>

    @Query("SELECT * FROM StockMine ")
    fun getMyStockFlow(): Flow<List<StockMine>>


    @Query("SELECT * FROM StockInfo Where stockId In(:ids) ")
    fun getStockInfoFlow(ids: List<String>): Flow<List<StockInfo>>


    @Query("SELECT * FROM StockMine Where stockId= :stockId ")
    suspend fun getStockMine(stockId : String): StockMine?


    @Query("SELECT stockId FROM StockInfo  ")
    suspend fun getStockIds() : List<String>

    @Query("SELECT stockId FROM StockMine  ")
    suspend fun getMyStockIds() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stockInfo: List<StockInfo>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stockInfo: StockInfo)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(word: StockNote)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrade(trade: StockTrade)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMyStock(stockMine: StockMine)



}