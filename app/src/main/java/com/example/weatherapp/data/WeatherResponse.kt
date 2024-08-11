package com.example.weatherapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val name: String
)

data class Main(
    val temp: Double,
    val humidity: Int
)

data class Weather(
    val description: String,
    val icon: String
)

@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cityName: String,
    val temperature: Double,
    val description: String,
    val icon: String,
    val timestamp: Long = System.currentTimeMillis()
)
