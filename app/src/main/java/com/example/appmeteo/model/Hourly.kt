package com.example.appmeteo.model

data class Hourly (
    val time: List<String>,
    val weathercode: List<Int>,
    val temperature_2m: List<Double>,
    val apparent_temperature: List<Double>,
)