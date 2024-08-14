package com.example.weatherapp.data

class LocalWeatherRepository(private val weatherDao: WeatherDao) {

    suspend fun insertWeather(weatherEntity: WeatherEntity) {
        weatherDao.insertWeather(weatherEntity)
    }

    suspend fun getLatestWeather(): WeatherEntity? {
        return weatherDao.getLatestWeather()
    }
}