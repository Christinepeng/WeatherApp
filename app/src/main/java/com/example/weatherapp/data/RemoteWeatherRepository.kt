package com.example.weatherapp.data

import android.util.Log

class RemoteWeatherRepository(private val apiService: WeatherApiService) {

    suspend fun fetchWeatherFromApi(cityLat: Double, cityLon: Double, apiKey: String): WeatherEntity {
        val response = apiService.getCurrentWeather(44.34, 10.99, "f5f9f068f617f0e2e1c8597573c700c0")
        return WeatherEntity(
            cityName = response.name,
            temperature = response.main.temp,
            description = response.weather[0].description,
            icon = response.weather[0].icon
        )
    }
}
