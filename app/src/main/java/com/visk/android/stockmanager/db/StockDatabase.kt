package com.visk.android.stockmanager.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.visk.android.stockmanager.db.dao.StockDao
import com.visk.android.stockmanager.db.dao.UserDao
import com.visk.android.stockmanager.db.entity.StockInfo
import com.visk.android.stockmanager.db.entity.StockNote
import com.visk.android.stockmanager.db.entity.User


@Database(entities = arrayOf(StockInfo::class, StockNote::class, User::class), version = 2 , exportSchema = true)
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
                ).addMigrations(MIGRATION_1_2).enableMultiInstanceInvalidation().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
        val MIGRATION_1_2 = object : Migration(1, 2) { // https://developer.android.com/training/data-storage/room/migrating-db-versions?hl=ko
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                CREATE TABLE `StockInfo_temp` (`stockId` TEXT NOT NULL, `name` TEXT NOT NULL, `currentPrice` INTEGER NOT NULL, `yesterdayPrice` INTEGER NOT NULL, `tradeVolume` INTEGER NOT NULL, `diffPercent` REAL NOT NULL, `todayHigh` INTEGER NOT NULL DEFAULT '0', `todayLow` INTEGER NOT NULL DEFAULT '0', `updateTime` TEXT NOT NULL, PRIMARY KEY(`stockId`))
                """.trimIndent())
                database.execSQL("""
                INSERT INTO StockInfo_temp (stockId, name, currentPrice, yesterdayPrice, tradeVolume, diffPercent, updateTime)
                SELECT stockId, name, currentPrice, yesterdayPrice, tradeVolume, diffPercent, updateTime FROM StockInfo
                """.trimIndent())
                database.execSQL("DROP TABLE StockInfo")
                database.execSQL("ALTER TABLE StockInfo_temp RENAME TO StockInfo")
            }
        }
    }

}