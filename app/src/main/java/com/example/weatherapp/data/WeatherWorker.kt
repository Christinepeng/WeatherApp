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
        // Initialize both repositories
        val localWeatherRepository = LocalWeatherRepository(
            WeatherDatabase.getDatabase(applicationContext).weatherDao()
        )
        val remoteWeatherRepository = RemoteWeatherRepository(RetrofitInstance.api)

        try {
            val weatherEntity = remoteWeatherRepository.fetchWeatherFromApi(44.34, 10.99,"f5f9f068f617f0e2e1c8597573c700c0")
            localWeatherRepository.insertWeather(weatherEntity)
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}

fun scheduleWeatherWork(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<WeatherWorker>(6, TimeUnit.HOURS)
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}
