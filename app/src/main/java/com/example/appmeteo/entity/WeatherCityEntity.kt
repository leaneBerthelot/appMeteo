package com.example.appmeteo.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appmeteo.model.Geometry

@Entity(tableName = "city_table")
data class WeatherCityEntity(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: String,
    val address: String,
    val name: String,
    val code: String,
    val latitude: Float,
    val longitude: Float,
)