package com.example.weatherapp.data

import com.example.weatherapp.BuildConfig

class RemoteWeatherRepository(private val apiService: WeatherApiService) {

    suspend fun fetchWeatherFromApi(cityLat: Double, cityLon: Double, apiKey: String): WeatherEntity {
        require(apiKey.isNotBlank()) { "Configure OPENWEATHER_API_KEY before requesting weather" }
        val response = apiService.getCurrentWeather(cityLat, cityLon, apiKey)
        return WeatherEntity(
            cityName = response.name,
            temperature = response.main.temp,
            description = response.weather[0].description,
            icon = response.weather[0].icon
        )
    }

    suspend fun searchCities(query: String): CitySearchResponse {
        require(BuildConfig.OPENWEATHER_API_KEY.isNotBlank()) {
            "Configure OPENWEATHER_API_KEY before searching cities"
        }
        return apiService.searchCities(query, BuildConfig.OPENWEATHER_API_KEY)
    }
}
