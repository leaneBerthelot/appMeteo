package com.example.appmeteo.model

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIInterface {

    @GET("/v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("hourly") hourly: String = "temperature_2m,apparent_temperature,weathercode",
        @Query("daily")  daily: String = "weathercode,temperature_2m_max,temperature_2m_min",
        @Query("forecast_days") forecast_days: Int = 14,
        @Query("timezone") timezone: String = "Europe/London",
    ): Response<WeatherResponse>

}