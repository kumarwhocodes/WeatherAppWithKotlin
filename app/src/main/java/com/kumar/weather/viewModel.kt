package com.kumar.weather

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import com.kumar.weather.network.WeatherApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class WeatherCondition {
    CLEAR,
    RAIN,
    CLOUDS,
    SNOW,
    FOG,
    MIST,
    THUNDERSTORM,
    HAZE
}

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

    private val _weatherCond = MutableStateFlow(WeatherCondition.CLEAR)
    val weatherCond: StateFlow<WeatherCondition> = _weatherCond

    private val _updateTime = MutableStateFlow("")
    val updateTime: StateFlow<String> = _updateTime

    private val _sunriseTime = MutableStateFlow("")
    val sunriseTime: StateFlow<String> = _sunriseTime

    private val _sunsetTime = MutableStateFlow("")
    val sunsetTime: StateFlow<String> = _sunsetTime

    private val _windSpeed = MutableStateFlow(0.0)
    val windSpeed: StateFlow<Double> = _windSpeed

    private val _iconUrl = MutableStateFlow("")
    val iconUrl: StateFlow<String> = _iconUrl

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
                val weatherCondition = weatherData.weather?.get(0)?.main ?: ""
                val iconCode = weatherData.weather?.get(0)?.main ?: ""

                _weatherCond.value = mapWeatherCondition(weatherCondition)
                _temperature.value = temperature - 273
                _mintemp.value = mintemp - 273
                _showCity.value = weatherData.name ?: ""
                _maxtemp.value = maxtemp - 273
                _iconUrl.value = "https://openweathermap.org/img/wn/${weatherData.weather?.get(0)?.icon}@2x.png"

                // Extract and update sunrise time
                weatherData.sys?.sunrise?.let { sunriseTimestamp ->
                    _sunriseTime.value = formatTime(sunriseTimestamp)
                }

                // Extract and update sunset time
                weatherData.sys?.sunset?.let { sunsetTimestamp ->
                    _sunsetTime.value = formatTime(sunsetTimestamp)
                }

                // Extract and update wind speed
                weatherData.wind?.speed?.let { speed ->
                    _windSpeed.value = speed
                }

                weatherData.dt?.let { lastUpdatedTimestamp ->
                    _updateTime.value = SimpleDateFormat("HH:mm", Locale.getDefault())
                        .format(Date(lastUpdatedTimestamp * 1000L))
                }


            } catch (e: Exception) {
                Log.e("error", "Error fetching temperature data: ${e.message}")


            }

        }

    }


    private fun formatTime(timestamp: Int): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date(timestamp * 1000L) // Convert Unix timestamp to milliseconds
        return sdf.format(date)
    }

    private fun mapWeatherCondition(condition: String): WeatherCondition {
        return when (condition) {
            "Clear" -> WeatherCondition.CLEAR
            "Clouds" -> WeatherCondition.CLOUDS
            "Rain" -> WeatherCondition.RAIN
            "Snow" -> WeatherCondition.SNOW
            "Fog" -> WeatherCondition.FOG
            "Mist" -> WeatherCondition.MIST
            "Thunderstorm" -> WeatherCondition.THUNDERSTORM
            "Haze" -> WeatherCondition.HAZE

            else -> WeatherCondition.CLEAR
        }
    }

//    @Composable
//    fun ShowToast(message: String) {
//        val context = LocalContext.current
//        val toast = remember { Toast.makeText(context, message, Toast.LENGTH_SHORT) }
//        toast.show()
//    }

}