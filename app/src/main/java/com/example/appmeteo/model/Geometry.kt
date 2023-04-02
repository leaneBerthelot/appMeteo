package com.example.appmeteo.model

data class Geometry(
    val coordinates: List<Float>
) {
    operator fun get(i: Int): Any {
        return coordinates[i]
    }
}