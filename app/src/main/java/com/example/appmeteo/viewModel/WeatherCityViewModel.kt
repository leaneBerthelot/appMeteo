package com.example.appmeteo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmeteo.dao.WeatherCityDao
import com.example.appmeteo.entity.WeatherCityEntity
import com.example.appmeteo.model.CityResponse
import com.example.appmeteo.services.CityRetrofitBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class WeatherCityViewModel : ViewModel()  {
    val retrofitBuilder = CityRetrofitBuilder.apiService
    private val _responseCity = MutableLiveData<Response<CityResponse>>()
    val responseCity: LiveData<Response<CityResponse>> = _responseCity

    private val _favoriteCities = MutableLiveData<List<WeatherCityEntity>>()
    val favoriteCities: LiveData<List<WeatherCityEntity>> = _favoriteCities

    fun getResponse (city :String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofitBuilder.getCity(city)
            withContext(Dispatchers.Main) {
                _responseCity.value = response
            }
        }
    }

    //get city from bd
    /*fun getFavCity (weatherCityDao : WeatherCityDao) {
        CoroutineScope(Dispatchers.IO).launch {
            val favoriteCitiesFromDb = weatherCityDao.getWeatherCities()
            withContext(Dispatchers.Main) {
                _favoriteCities.postValue(favoriteCitiesFromDb)
            }
        }
    }*/
}