package com.example.appmeteo.services

import com.example.appmeteo.model.CityAPIInterface
import com.example.appmeteo.model.WeatherAPIInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CityRetrofitBuilder {

        val BASE_URL = "https://api-adresse.data.gouv.fr/"

        fun getCityResponse() : Retrofit {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

        }

        val apiService: CityAPIInterface = getCityResponse().create(CityAPIInterface::class.java)

    }