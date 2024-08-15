package com.example.weatherapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
data class CityEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val lat: Double,
    val lon: Double,
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int?,
    val grnd_level: Int?,
    val country: String,
    val wind_speed: Double,
    val wind_deg: Int,
    val cloudiness: Int,
    val weather_main: String,
    val weather_description: String,
    val weather_icon: String,
    val timestamp: Long
)

