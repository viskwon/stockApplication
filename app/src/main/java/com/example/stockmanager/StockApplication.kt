package com.example.stockmanager

import android.app.Application
import com.example.stockmanager.db.UserRoomDatabase
import com.example.stockmanager.repository.UserRepository

class StockApplication :Application(){

    val database by lazy { UserRoomDatabase.getDatabase(this) }
    val repository by lazy { UserRepository(database.userDao()) }

}