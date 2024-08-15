package com.example.weatherapp.data

data class CitySearchResponse(
    val message: String,
    val cod: String,
    val count: Int,
    val list: List<City>
)

data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val main: Main1,
    val dt: Long,
    val sys: Sys,
    val wind: Wind,
    val clouds: Clouds,
    val weather: List<Weather1>
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class Main1(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int?,
    val grnd_level: Int?
)

data class Sys(
    val country: String
)

data class Wind(
    val speed: Double,
    val deg: Int
)

data class Clouds(
    val all: Int
)

data class Weather1(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

