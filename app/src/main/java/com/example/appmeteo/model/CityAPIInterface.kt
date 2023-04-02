package com.example.appmeteo.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CityAPIInterface {

    @GET("/search")
    suspend fun getCity(
        @Query("q") q: String,
        @Query("limit") hourly: Int = 15,
    ): Response<CityResponse>

}