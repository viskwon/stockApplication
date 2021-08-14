package com.visk.android.stockmanager.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.visk.android.stockmanager.db.User
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {
    @Query("SELECT * FROM User ")
    fun getAlphabetizedWords(): List<User>

    @Query("SELECT * FROM User ")
    fun getUserFlow(): Flow<List<User>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: User)

    @Query("DELETE FROM User")
    suspend fun deleteAll()

}