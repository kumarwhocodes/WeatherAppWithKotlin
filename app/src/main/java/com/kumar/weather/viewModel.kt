package com.kumar.weather

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kumar.weather.network.WeatherAPI
import com.kumar.weather.network.WeatherApiService

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.lang.Exception

class WeatherViewModel() : ViewModel() {

    private val _inputCity = MutableStateFlow("")
    val inputCity: StateFlow<String> = _inputCity

    private val _showCity = MutableStateFlow("")
    val showCity: StateFlow<String> = _showCity

    private val _temperature = MutableStateFlow(0)
    val temperature: StateFlow<Int> = _temperature

    private val _mintemp = MutableStateFlow(0)
    val mintemp: StateFlow<Int> = _mintemp

    private val _maxtemp = MutableStateFlow(0)
    val maxtemp: StateFlow<Int> = _maxtemp

    fun onInputCityChanged(city: String) {
        _inputCity.value = city
    }

    fun onSearchClicked() {
        viewModelScope.launch {
            try {
                val weatherData =
                    WeatherApiService().getCurrentWeatherFromCityName(_inputCity.value)
                Log.d(TAG, "API Response: $weatherData")
                val temperature = weatherData.main?.temp?.toInt() ?: 0
                val mintemp = weatherData.main?.tempMin?.toInt() ?: 0
                val maxtemp = weatherData.main?.tempMax?.toInt() ?: 0
                _temperature.value = temperature - 273
                _mintemp.value = mintemp - 273
                _showCity.value = weatherData.name ?: ""
                _maxtemp.value = maxtemp - 273

            } catch (e: Exception) {
                Log.e("error", "Error fetching temperature data: ${e.message}")


            }

        }

    }

}