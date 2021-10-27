package com.visk.android.stockmanager.db

import android.content.Context
import android.util.Log
import androidx.collection.ArrayMap
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.visk.android.stockmanager.db.StockDatabase.Companion.DATABASE_VERSION
import com.visk.android.stockmanager.db.dao.StockDao
import com.visk.android.stockmanager.db.dao.UserDao
import com.visk.android.stockmanager.db.entity.*


@Database(entities = arrayOf(StockInfo::class, StockNote::class, User::class , StockTrade::class,StockMine::class), version = DATABASE_VERSION , exportSchema = true)
public abstract class StockDatabase  : RoomDatabase(){

    abstract fun userDao() : UserDao
    abstract fun stockDao() : StockDao

    companion object {
        const val DATABASE_NAME = "stock_database"
        const val DATABASE_VERSION = 5
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
                    DATABASE_NAME
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4,MIGRATION_4_5).enableMultiInstanceInvalidation().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
        val MIGRATION_1_2 = object : Migration(1, 2) { // https://developer.android.com/training/data-storage/room/migrating-db-versions?hl=ko
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.d("hjskwon","mi 12")
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

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.d("hjskwon","mi 23")
                database.execSQL("""
                CREATE TABLE `StockTrade` (`stockId` TEXT NOT NULL, `volumn` INTEGER NOT NULL, `price` INTEGER NOT NULL, `date` TEXT NOT NULL, `tradeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)
                """.trimIndent())
            }
        }
        val MIGRATION_3_4 = object : Migration(3,4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.d("hjskwon","mi 34")
                database.execSQL("""
                CREATE TABLE `StockMine` (`stockId` TEXT NOT NULL, `volumn` INTEGER NOT NULL, `price` INTEGER NOT NULL, `startDate` TEXT NOT NULL, PRIMARY KEY(`stockId`))
                """.trimIndent())
            }
        }

        val MIGRATION_4_5 = object : Migration(4,5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Log.d("hjskwon","mi 45")
                database.query("")
                database.execSQL("""
                CREATE TABLE `StockMineTemp` (`stockId` TEXT NOT NULL, `volumn` INTEGER NOT NULL, `totalPrice` INTEGER NOT NULL DEFAULT '0', `startDate` TEXT NOT NULL, PRIMARY KEY(`stockId`))
                """.trimIndent())
                database.execSQL("""
                INSERT INTO StockMineTemp (stockId, volumn, startDate)
                SELECT stockId, volumn, startDate FROM StockMine
                """.trimIndent())
                database.execSQL("DROP TABLE StockMine")
                database.execSQL("ALTER TABLE StockMineTemp RENAME TO StockMine")
                val cursor = database.query("SELECT * FROM StockTrade")
                val pair = ArrayMap<String , Int>()


                if(cursor.moveToFirst()){
                    var stock = cursor.getString(cursor.getColumnIndex("stockId"))
                    var vol = cursor.getInt(cursor.getColumnIndex("volumn"))
                    var price = cursor.getInt(cursor.getColumnIndex("price"))
                    pair.get(stock)?.run {  }?: pair.put(stock,price*vol)
                    while (cursor.moveToNext()){
                        stock = cursor.getString(cursor.getColumnIndex("stockId"))
                        vol = cursor.getInt(cursor.getColumnIndex("volumn"))
                        price = cursor.getInt(cursor.getColumnIndex("price"))
                        pair.get(stock)?.let { pair.put(stock, price * vol + it) } ?: pair.put(
                            stock,
                            price * vol
                        )
                    }
                    pair.forEach {
                        Log.d("hjskwon","${it.key} ${it.value}")
                        Log.d("hjskwon","UPDATE StockMine SET totalPrice = ${it.value} WHERE stockId = \"${it.key}\"")
                        Log.d("hjskwon","UPDATE StockMine SET totalPrice = ${it.value} WHERE stockId = '${it.key}'")

                        database.execSQL("UPDATE StockMine SET totalPrice = ${it.value} WHERE stockId = \"${it.key}\"")
                    }
                }

            }
        }
    }

}