package com.example.weatherapp.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.WorkManager
import androidx.work.PeriodicWorkRequestBuilder
import java.util.concurrent.TimeUnit

class WeatherWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val repository = WeatherRepository(
            WeatherDatabase.getDatabase(applicationContext).weatherDao(),
            RetrofitInstance.api
        )

        try {
            repository.refreshWeather("CityName", "YourApiKey")
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}

// To schedule periodic work
fun scheduleWeatherWork(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<WeatherWorker>(6, TimeUnit.HOURS)
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}
