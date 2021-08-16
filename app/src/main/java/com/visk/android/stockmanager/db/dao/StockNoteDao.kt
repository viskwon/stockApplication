package com.visk.android.stockmanager.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.visk.android.stockmanager.db.StockNote
import com.visk.android.stockmanager.db.User
import kotlinx.coroutines.flow.Flow

@Dao
interface StockNoteDao {
    @Query("SELECT * FROM StockNote ")
    fun getAlphabetizedWords(): List<StockNote>

    @Query("SELECT * FROM StockNote ")
    fun getUserFlow(): Flow<List<StockNote>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: StockNote)

}