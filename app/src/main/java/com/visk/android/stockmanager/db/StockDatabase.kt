package com.visk.android.stockmanager.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.visk.android.stockmanager.db.dao.StockDao
import com.visk.android.stockmanager.db.dao.UserDao
import com.visk.android.stockmanager.db.entity.StockInfo
import com.visk.android.stockmanager.db.entity.StockNote
import com.visk.android.stockmanager.db.entity.User


@Database(entities = arrayOf(StockInfo::class, StockNote::class, User::class), version = 1 , exportSchema = true)
public abstract class StockDatabase  : RoomDatabase(){

    abstract fun userDao() : UserDao
    abstract fun stockDao() : StockDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: StockDatabase? = null

        fun getDatabase(context: Context): StockDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StockDatabase::class.java,
                    "stock_database"
                ).enableMultiInstanceInvalidation().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}