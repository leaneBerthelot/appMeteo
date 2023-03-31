package com.example.appmeteo.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmeteo.model.WeatherResponse
import com.example.appmeteo.services.RetrofitBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.util.*


class WeatherViewModel : ViewModel() {
    val retrofitBuilder = RetrofitBuilder.apiService
    private val _responseWeather = MutableLiveData<Response<WeatherResponse>>()
    val responseWeather: LiveData<Response<WeatherResponse>> = _responseWeather

    fun getResponse () {
        CoroutineScope(Dispatchers.IO).launch {
            val response = retrofitBuilder.getWeather(44.83F, -0.57F)
            withContext(Dispatchers.Main) {
                _responseWeather.value = response
            }
        }
    }

    fun getDescription(code: Int): String {
        return when(code) {
            0 -> "Dégagé"
            in listOf(1, 2, 3) -> "Nuageux"
            in listOf(45, 48) -> "Brouillard"
            in listOf(51, 53, 55, 56, 57) -> "Bruine"
            in listOf(61, 63, 65, 66, 67, 80, 81, 82) -> "Pluie"
            in listOf(71, 73, 75, 77, 85, 86) -> "Neige"
            in listOf(95, 96, 99) -> "Orage"
            else -> "error"
        }
    }

    fun getDay(day: Int, additionalDay : Int): String {
        val newDay = ((day + additionalDay) % 7 == 0).let { if (it) 7 else (day + additionalDay) % 7 }
        return when (newDay) {
            Calendar.MONDAY -> "Lundi"
            Calendar.TUESDAY -> "Mardi"
            Calendar.WEDNESDAY -> "Mercredi"
            Calendar.THURSDAY -> "Jeudi"
            Calendar.FRIDAY -> "Vendredi"
            Calendar.SATURDAY -> "Samedi"
            Calendar.SUNDAY -> "Dimanche"
            else -> ""
        }
    }
}