package com.visk.android.stockmanager

import android.app.Application
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.visk.android.stockmanager.db.UserRoomDatabase
import com.visk.android.stockmanager.repository.UserRepository
import com.visk.android.stockmanager.worker.StockRefreshWorker
import java.util.concurrent.TimeUnit

class StockApplication :Application(){

    val database by lazy { UserRoomDatabase.getDatabase(this) }
    val repository by lazy { UserRepository(database.userDao()) }

    fun startAutoRefresh(){
        val work =  PeriodicWorkRequestBuilder<StockRefreshWorker>(5L , TimeUnit.SECONDS).build()
        WorkManager.getInstance(applicationContext).enqueue(work)

    }

}