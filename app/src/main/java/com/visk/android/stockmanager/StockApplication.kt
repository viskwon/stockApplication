package com.visk.android.stockmanager

import android.app.Application
import com.visk.android.stockmanager.db.UserRoomDatabase
import com.visk.android.stockmanager.repository.UserRepository

class StockApplication :Application(){

    val database by lazy { UserRoomDatabase.getDatabase(this) }
    val repository by lazy { UserRepository(database.userDao()) }

}