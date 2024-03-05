package com.kumar.weather.network

import com.kumar.weather.OpenWeatherMap
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("weather")
    suspend fun getCurrentWeatherFromCityName(
        @Query("q") city: String,
        @Query("appid") key: String = "23c6ed0e8904274f86a38241a58dd017"
    ) : OpenWeatherMap


}