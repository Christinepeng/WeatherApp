package com.example.weatherapp.data

class WeatherRepository(private val weatherDao: WeatherDao, private val apiService: WeatherApiService) {

    suspend fun refreshWeather(cityName: String, apiKey: String) {
        val response = apiService.getCurrentWeather(cityName, apiKey)
        val weatherEntity = WeatherEntity(
            cityName = response.name,
            temperature = response.main.temp,
            description = response.weather[0].description,
            icon = response.weather[0].icon
        )
        weatherDao.insertWeather(weatherEntity)
    }

    suspend fun getLatestWeather(): WeatherEntity? {
        return weatherDao.getLatestWeather()
    }
}
