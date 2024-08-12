package com.example.weatherapp.ui

import android.os.Bundle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.data.RetrofitInstance
import com.example.weatherapp.data.WeatherDatabase
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.scheduleWeatherWork


class MainActivity : ComponentActivity() {
//    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var viewModel: WeatherViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = WeatherDatabase.getDatabase(applicationContext)
        val weatherDao = database.weatherDao()
        val repository = WeatherRepository(weatherDao, RetrofitInstance.api)
        val factory = WeatherViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(WeatherViewModel::class.java)

        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
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
    val weather = viewModel.weatherState.collectAsState().value

    weather?.let {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "City: ${weather.cityName}")
            Text(text = "Temperature: ${weather.temperature} °C")
            Text(text = "Description: ${weather.description}")
            Image(
                painter = painterResource(id = getWeatherIcon(weather.icon)),
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