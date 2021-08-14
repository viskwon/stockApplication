package com.example.roomuserinfo

import android.app.Application
import com.example.roomuserinfo.db.UserRoomDatabase
import com.example.roomuserinfo.repository.UserRepository

class StockApplication :Application(){

    val database by lazy { UserRoomDatabase.getDatabase(this) }
    val repository by lazy { UserRepository(database.userDao()) }

}