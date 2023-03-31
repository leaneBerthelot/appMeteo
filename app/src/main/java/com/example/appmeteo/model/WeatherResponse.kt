package com.example.appmeteo.model

data class WeatherResponse (
    val timezone: String,
    val timezone_abbreviation: String,
    val elevation: Double,
    val hourly: Hourly,
    val daily: Daily
)

