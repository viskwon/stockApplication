package com.visk.android.stockmanager.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.visk.android.stockmanager.db.dao.UserDao


@Database(entities = arrayOf(User::class), version = 1 , exportSchema = true)
public abstract class UserRoomDatabase  : RoomDatabase(){

    abstract fun userDao() : UserDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: UserRoomDatabase? = null

        fun getDatabase(context: Context): UserRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserRoomDatabase::class.java,
                    "user_database"
                ).enableMultiInstanceInvalidation().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }



}