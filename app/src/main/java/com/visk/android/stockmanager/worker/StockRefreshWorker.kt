package com.visk.android.stockmanager.worker

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.visk.android.stockmanager.R
import com.visk.android.stockmanager.StockApplication
import com.visk.android.stockmanager.db.StockDatabase
import com.visk.android.stockmanager.repository.StockRepository
import com.visk.android.stockmanager.stock.StockRemoteDataSource

class StockRefreshWorker (appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {
    val database by lazy { StockDatabase.getDatabase(appContext) }
    val stockRepository by lazy { StockRepository(StockRemoteDataSource(), database.stockDao()) }

    override suspend fun doWork(): Result {
        Log.d("hjskwon","hjskwon doWork")
        val stockList = stockRepository.requestStockInfo()

        val nono = NotificationCompat.Builder(applicationContext, "priceNoti")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("title")
            .setContentText("content")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            Log.d("hjskwon","hjskwon here 2")
            notify(31232, nono.build())
        }

        if(stockList.get(0).currentPrice > 29500){
            Log.d("hjskwon","hjskwon here")
             val noti = NotificationCompat.Builder(applicationContext, "priceNoti")
                 .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(stockList.get(0).name)
                .setContentText("${stockList.get(0).currentPrice}")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(applicationContext)) {
                // notificationId is a unique int for each notification that you must define
                Log.d("hjskwon","hjskwon here 2")
                notify(stockList.get(0).stockId.toInt(), noti.build())
            }
        }

        return Result.success()
    }

}