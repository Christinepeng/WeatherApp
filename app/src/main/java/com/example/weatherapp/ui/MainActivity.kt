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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.data.City
import com.example.weatherapp.data.CitySearchResponse
import com.example.weatherapp.data.LocalWeatherRepository
import com.example.weatherapp.data.RemoteWeatherRepository
import com.example.weatherapp.data.RetrofitInstance
import com.example.weatherapp.data.WeatherDatabase
import com.example.weatherapp.data.scheduleWeatherWork
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
//                    WeatherScreen(viewModel)
                    GoogleLikeSearchScreen(viewModel)
                }
            }
        }
        scheduleWeatherWork(applicationContext)
    }
}

@Composable
fun GoogleLikeSearchScreen(viewModel: WeatherViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf(CitySearchResponse("", "", 0, mutableListOf<City>()))}
//    var suggestions by remember { mutableStateOf(listOf<String>()) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        // Search bar
        BasicTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                scope.launch {
                    delay(300) // Debounce API calls
                    if (searchQuery.isNotEmpty()) {
                        suggestions = viewModel.getSuggestions(query)
                    } else {
//                        suggestions = emptyList()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.LightGray)
                .padding(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // Handle search action
                }
            )
        )

        // Suggestions dropdown
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(3) { index ->
//                items(suggestions.size) { index ->
                Text(
                    text = "text",
//                    text = suggestions[index],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
//                            searchQuery = suggestions[index]
//                            searchQuery = suggestions[index]
//                            suggestions = emptyList()
                        }
                )
            }
        }
    }
}

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val weather by viewModel.weatherState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Column {
        SearchBar(
            query = searchQuery,
            onQueryChanged = { searchQuery = it }
        )


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
}

fun getWeatherIcon(iconCode: String): Int {
    return when (iconCode) {
        "01d" -> com.example.weatherapp.R.drawable.ic_clear_day
        "02d" -> com.example.weatherapp.R.drawable.ic_partly_cloudy_day
        else -> com.example.weatherapp.R.drawable.ic_cloudy
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChanged,
            placeholder = { Text(text = "Search...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFE0E0E0), // Background color of the search bar
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        WeatherScreen(viewModel = viewModel())
    }
}