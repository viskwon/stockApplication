package com.visk.android.stockmanager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
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

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startAutoRefresh()
    }
    fun startAutoRefresh(){
        val work =  PeriodicWorkRequestBuilder<StockRefreshWorker>(15 , TimeUnit.MINUTES).build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork("periodic", ExistingPeriodicWorkPolicy.KEEP, work)

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = "priceNoti"
        val descriptionText = "priceNoti"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("priceNoti", name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }



}