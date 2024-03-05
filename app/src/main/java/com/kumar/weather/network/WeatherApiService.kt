package com.kumar.weather.network

import com.kumar.weather.OpenWeatherMap
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private const val BASE_URL="https://api.openweathermap.org/data/2.5/"

class WeatherApiService {
    val weatherapi:WeatherAPI

    init {

         val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

            weatherapi=retrofit.create(WeatherAPI::class.java)
    }

    suspend fun getCurrentWeatherFromCityName(city:String) : OpenWeatherMap{
        return weatherapi.getCurrentWeatherFromCityName(city,"23c6ed0e8904274f86a38241a58dd017")
    }

}