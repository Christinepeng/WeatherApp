package com.example.weatherapp.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.WorkManager
import androidx.work.PeriodicWorkRequestBuilder
import com.example.weatherapp.BuildConfig
import java.util.concurrent.TimeUnit

class WeatherWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val localWeatherRepository = LocalWeatherRepository(
            WeatherDatabase.getDatabase(applicationContext).weatherDao()
        )
        val remoteWeatherRepository = RemoteWeatherRepository(RetrofitInstance.api)

        return try {
            val weatherEntity = remoteWeatherRepository.fetchWeatherFromApi(
                44.34,
                10.99,
                BuildConfig.OPENWEATHER_API_KEY
            )
            localWeatherRepository.insertWeather(weatherEntity)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}

fun scheduleWeatherWork(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<WeatherWorker>(6, TimeUnit.HOURS)
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}
