package com.visk.android.stockmanager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.visk.android.stockmanager.db.StockDatabase
import com.visk.android.stockmanager.repository.StockRepository
import com.visk.android.stockmanager.worker.StockRefreshWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class StockApplication :Application(), Configuration.Provider{
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    @Inject
    lateinit var stockRepository: StockRepository

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startAutoRefresh()
    }
    fun startAutoRefresh(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()
        val work =
            PeriodicWorkRequestBuilder<StockRefreshWorker>(15, TimeUnit.MINUTES).setBackoffCriteria(
                BackoffPolicy.LINEAR,
                10,
                TimeUnit.SECONDS
            ).build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork("periodic", ExistingPeriodicWorkPolicy.REPLACE, work)

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