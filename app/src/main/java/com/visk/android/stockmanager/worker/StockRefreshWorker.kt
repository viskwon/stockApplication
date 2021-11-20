package com.visk.android.stockmanager.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.visk.android.stockmanager.R
import com.visk.android.stockmanager.repository.StockRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

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


        with(NotificationManagerCompat.from(applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            Log.d("hjskwon","hjskwon here 2")
            notify(31232, nono.build())
        }
        stockRepository.requestStockInfo()
            .onSuccess {
                if (it.isNotEmpty()) {
                    Log.d("hjskwon", "hjskwon here 3")
                    val noti = NotificationCompat.Builder(applicationContext, "priceNoti")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(it.get(0).name)
                        .setContentText("${it.get(0).currentPrice}")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                    with(NotificationManagerCompat.from(applicationContext)) {
                        // notificationId is a unique int for each notification that you must define
                        Log.d("hjskwon", "hjskwon here 4")
                        notify(it.get(0).stockId.toInt(), noti.build())
                    }
                }
                return Result.success()
            }.onFailure {

            val nono = NotificationCompat.Builder(applicationContext, "priceNoti")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("retry")
                .setContentText("retry")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)


            with(NotificationManagerCompat.from(applicationContext)) {
                // notificationId is a unique int for each notification that you must define
                notify(System.currentTimeMillis().hashCode(), nono.build())
            }
            return Result.retry()
        }
        return Result.success()
    }


    override suspend fun getForegroundInfo(): ForegroundInfo {
        /*     val id = applicationContext.getString(R.string.notification_channel_id)
     val title = applicationContext.getString(R.string.notification_title)
     val cancel = applicationContext.getString(R.string.cancel_download)*/
        // This PendingIntent can be used to cancel the worker
/*
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())
*/


        val notification = NotificationCompat.Builder(applicationContext, "priceNoti")
            .setContentTitle("title")
            .setContentText("yyy")
            .setSmallIcon(R.drawable.launch_ic)
            .setOngoing(true)
            // Add the cancel action to the notification which can
            // be used to cancel the worker
//            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()

        return ForegroundInfo(32135,notification)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = "backnoti"
        val descriptionText = "backnoti"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("backnoti", name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

}