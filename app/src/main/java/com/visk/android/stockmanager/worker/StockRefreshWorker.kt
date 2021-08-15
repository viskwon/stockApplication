package com.visk.android.stockmanager.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.visk.android.stockmanager.repository.StockRepository

class StockRefreshWorker (appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    val stockList = listOf("005930", "027740", "068270", "032350")
    override suspend fun doWork(): Result {
        StockRepository.getInstance().requestStockInfo(stockList)
        return Result.success()
    }

}