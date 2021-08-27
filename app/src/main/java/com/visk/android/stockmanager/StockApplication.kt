package com.visk.android.stockmanager

import android.app.Application
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.visk.android.stockmanager.db.StockDatabase
import com.visk.android.stockmanager.repository.StockRepository
import com.visk.android.stockmanager.repository.UserRepository
import com.visk.android.stockmanager.stock.StockRemoteDataSource
import com.visk.android.stockmanager.worker.StockRefreshWorker
import java.util.concurrent.TimeUnit

class StockApplication :Application(){

    val database by lazy { StockDatabase.getDatabase(this) }
    val stockRepository by lazy { StockRepository(StockRemoteDataSource(), database.stockDao()) }

    fun startAutoRefresh(){
        val work =  PeriodicWorkRequestBuilder<StockRefreshWorker>(5L , TimeUnit.SECONDS).build()
        WorkManager.getInstance(applicationContext).enqueue(work)

    }

}