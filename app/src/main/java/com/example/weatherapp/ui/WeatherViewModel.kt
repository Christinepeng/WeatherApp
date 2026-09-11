package com.example.weatherapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.CitySearchResponse
import com.example.weatherapp.data.LocalWeatherRepository
import com.example.weatherapp.data.RemoteWeatherRepository
import com.example.weatherapp.data.WeatherEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val localWeatherRepository: LocalWeatherRepository,
    private val remoteWeatherRepository: RemoteWeatherRepository
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherEntity?>(null)
    val weatherState: StateFlow<WeatherEntity?> get() = _weatherState

    fun refreshWeather(cityLat: Double, cityLon: Double, apiKey: String) {
        viewModelScope.launch {
            val response = remoteWeatherRepository.fetchWeatherFromApi(cityLat, cityLon, apiKey)
            val weatherEntity = WeatherEntity(
                cityName = response.cityName,
                temperature = response.temperature,
                description = response.description,
                icon = response.icon
            )
            localWeatherRepository.insertWeather(weatherEntity)
            _weatherState.value = weatherEntity
        }
    }

    suspend fun getSuggestions(query: String): CitySearchResponse {
        return remoteWeatherRepository.searchCities(query)
    }

    init {
        viewModelScope.launch {
            if (BuildConfig.OPENWEATHER_API_KEY.isNotBlank()) {
                _weatherState.value = remoteWeatherRepository.fetchWeatherFromApi(
                    44.34,
                    10.99,
                    BuildConfig.OPENWEATHER_API_KEY
                )
            }
        }
    }
}

class WeatherViewModelFactory(
    private val localRepository: LocalWeatherRepository,
    private val remoteRepository: RemoteWeatherRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(localRepository, remoteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
