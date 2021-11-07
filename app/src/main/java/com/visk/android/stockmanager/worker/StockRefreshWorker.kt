package com.visk.android.stockmanager.worker

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.visk.android.stockmanager.R
import com.visk.android.stockmanager.StockApplication
import com.visk.android.stockmanager.db.StockDatabase
import com.visk.android.stockmanager.repository.StockRepository
import com.visk.android.stockmanager.stock.StockRemoteDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class StockRefreshWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val stockRepository: StockRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("hjskwon","hjskwon doWork $stockRepository")
        val nono = NotificationCompat.Builder(applicationContext, "priceNoti")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("title")
            .setContentText("content")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val stockList = stockRepository.requestStockInfo()


        with(NotificationManagerCompat.from(applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            Log.d("hjskwon","hjskwon here 2")
            notify(31232, nono.build())
        }

        if(stockList.isNotEmpty() ){
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