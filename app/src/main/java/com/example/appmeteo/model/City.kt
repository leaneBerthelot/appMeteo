package com.example.appmeteo.model

data class City(
    val Id: String,
    val Address: String,
    val Name: String,
    val Code: String,
    val Coordinates: Geometry?
)