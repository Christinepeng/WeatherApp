package com.example.weatherapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.data.LocalWeatherRepository
import com.example.weatherapp.data.RemoteWeatherRepository
import com.example.weatherapp.data.RetrofitInstance
import com.example.weatherapp.data.WeatherDatabase
import com.example.weatherapp.data.scheduleWeatherWork


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = WeatherDatabase.getDatabase(applicationContext)
        val weatherDao = database.weatherDao()
        val apiService = RetrofitInstance.api
        val localRepository = LocalWeatherRepository(weatherDao)
        val remoteRepository = RemoteWeatherRepository(apiService)
        val viewModelFactory = WeatherViewModelFactory(localRepository, remoteRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)


        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen(viewModel)
                }
            }
        }
        scheduleWeatherWork(applicationContext)
    }
}

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val weather by viewModel.weatherState.collectAsState()

    weather?.let {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "City: ${weather!!.cityName}")
            Text(text = "Temperature: ${weather!!.temperature} °C")
            Text(text = "Description: ${weather!!.description}")
            Image(
                painter = painterResource(id = getWeatherIcon(weather!!.icon)),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}

fun getWeatherIcon(iconCode: String): Int {
    return when (iconCode) {
        "01d" -> com.example.weatherapp.R.drawable.ic_clear_day
        "02d" -> com.example.weatherapp.R.drawable.ic_partly_cloudy_day
        else -> com.example.weatherapp.R.drawable.ic_cloudy
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        WeatherScreen(viewModel = viewModel())
    }
}