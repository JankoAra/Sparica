package com.example.sparica.util

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.sparica.SparicaApp
import com.example.sparica.data.api.ExchangeRateAPI
import com.example.sparica.data.repositories.impl.ExchangeRateRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class DailyExchangeRateFetchWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        //val db = SparicaDatabase.getDatabase(applicationContext, CoroutineScope(Dispatchers.IO))
        Log.d("DailyTaskWorker", "Work started.")
        val app = applicationContext as SparicaApp
        val db = app.db
        val exchangeRateDao = db.exchangeRateDao()
        val exchangeRateRepo = ExchangeRateRepositoryImpl(exchangeRateDao, ExchangeRateAPI)

        return withContext(Dispatchers.IO) {
            try {
                exchangeRateRepo.getLatestRates()
                Log.d("DailyTaskWorker", "Work finished.")
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }


    }

}

fun scheduleDailyExchangeRateFetch(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED) //wifi
        .build()

    val dailyWorkRequest =
        PeriodicWorkRequestBuilder<DailyExchangeRateFetchWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "Exchange rates API fetch",
        ExistingPeriodicWorkPolicy.KEEP,
        dailyWorkRequest
    )
}