package com.example.appmeteo.services

import com.example.appmeteo.model.WeatherAPIInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    val BASE_URL = "https://api.open-meteo.com/"

    private fun getWeatherResponse() : Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

    }

    val apiService: WeatherAPIInterface = getWeatherResponse().create(WeatherAPIInterface::class.java)

}