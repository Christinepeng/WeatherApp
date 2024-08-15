package com.example.weatherapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
            _weatherState.value = weatherEntity        }
    }

    suspend fun getSuggestions(query: String): CitySearchResponse {
        return remoteWeatherRepository.searchCities(query)
    }

//    suspend fun getSuggestions(query: String): List<String> {
//        return remoteWeatherRepository.searchCities(query).map { it.name }
//    }

    init {
        viewModelScope.launch {
//            _weatherState.value = localWeatherRepository.getLatestWeather()
            _weatherState.value = remoteWeatherRepository.fetchWeatherFromApi(44.34, 10.99,"f5f9f068f617f0e2e1c8597573c700c0")
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
