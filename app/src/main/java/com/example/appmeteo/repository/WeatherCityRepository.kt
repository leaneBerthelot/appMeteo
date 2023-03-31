package com.example.appmeteo.repository

import androidx.annotation.WorkerThread
import com.example.appmeteo.dao.WeatherCityDao
import com.example.appmeteo.entity.WeatherCityEntity
import kotlinx.coroutines.flow.Flow

class WeatherCityRepository(private val wordDao: WeatherCityDao) {
    val allCities: Flow<List<WeatherCityEntity>> = wordDao.getWeatherCities()

    @WorkerThread
    suspend fun insert(city: WeatherCityEntity) {
        wordDao.insert(city)
    }
}