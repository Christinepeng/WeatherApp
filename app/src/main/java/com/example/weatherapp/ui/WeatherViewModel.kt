package com.example.weatherapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherEntity
import com.example.weatherapp.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherEntity?>(null)
    val weatherState: StateFlow<WeatherEntity?> get() = _weatherState

    fun refreshWeather(cityName: String, apiKey: String) {
        viewModelScope.launch {
            repository.refreshWeather(cityName, apiKey)
            _weatherState.value = repository.getLatestWeather()
        }
    }

    init {
        viewModelScope.launch {
            _weatherState.value = repository.getLatestWeather()
        }
    }
}

class WeatherViewModelFactory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
