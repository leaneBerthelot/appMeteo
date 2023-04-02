package com.example.appmeteo.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appmeteo.R
import com.example.appmeteo.database.CityRoomDatabase
import com.example.appmeteo.model.HourPrev
import com.example.appmeteo.model.Prev
import com.example.appmeteo.repository.WeatherCityRepository
import com.example.appmeteo.viewModel.WeatherViewModel
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.time.LocalTime
import java.util.*

class WeatherActivity : AppCompatActivity() {

    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = getIntent()

        val latitude = intent.getFloatExtra("latitude", 44.8F)
        val longitude = intent.getFloatExtra("longitude", -0.5F)
        val city = intent.getStringExtra("city") ?: "Bordeaux"

        val currentHour = LocalTime.now().hour

        val textview_temp_min_max = findViewById<TextView>(R.id.textview_temp_min_max)
        val textview_temp = findViewById<TextView>(R.id.textview_temp)
        val textview_code_temp = findViewById<TextView>(R.id.textview_code_temp)
        val textview_temp_res = findViewById<TextView>(R.id.textview_temp_res)
        val textview_city = findViewById<TextView>(R.id.textview_city)

        textview_city.text = city

        val button_add = findViewById<Button>(R.id.button_add)

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        button_add.setOnClickListener {
            val intent = Intent(this, CityActivity::class.java)
            startActivity(intent)
        }

        viewModel.responseWeather.observe(this, Observer { response ->
                try {
                    if (response.isSuccessful) {
                        println("CALL API : ${response.body()}")

                        textview_temp_min_max.text = response.body()?.daily?.temperature_2m_min?.get(0)?.toString() + "°C/" + response.body()?.daily?.temperature_2m_max?.get(0)?.toString() + "°C"
                        textview_temp.text = response.body()?.hourly?.temperature_2m?.get(currentHour).toString()+ "°C"
                        textview_temp_res.text = response.body()?.hourly?.apparent_temperature?.get(currentHour).toString()+ "°C"
                        val weatherCode = response.body()?.hourly?.weathercode?.get(currentHour)
                        textview_code_temp.text = weatherCode?.let { viewModel.getDescription(it) }

                        val days = mutableListOf<Prev>()
                        val size = response.body()?.daily?.temperature_2m_min?.size ?: 0

                        val calendar = Calendar.getInstance()
                        val day = calendar.get(Calendar.DAY_OF_WEEK)

                        for (i in 0..size-1) {
                            days.add(Prev(
                                viewModel.getDay(day, i),
                                "${response.body()?.daily?.temperature_2m_min?.get(i)}°C/${response.body()?.daily?.temperature_2m_max?.get(i)}°C"
                            ))
                        }

                        val dayRecyclerView = findViewById<RecyclerView>(R.id.day_recycler_view)
                        dayRecyclerView.layoutManager = LinearLayoutManager(this)
                        dayRecyclerView.adapter = PrevAdapter(days)

                        val hours = mutableListOf<HourPrev>()

                        for (i in 0..23) {
                            val weatherCodeHour = response.body()?.hourly?.weathercode?.get(i)
                            val weatherCodeHourText = weatherCodeHour?.let { viewModel.getDescription(it).toString() }
                            hours.add(HourPrev(
                                weatherCodeHourText.toString(),
                                "${response.body()?.hourly?.temperature_2m?.get(i)}°C",
                                "${i}h"
                            ))
                        }

                        val hourRecyclerView = findViewById<RecyclerView>(R.id.hour_recycler_view)
                        hourRecyclerView.layoutManager = LinearLayoutManager(this)
                        hourRecyclerView.adapter = HourPrevAdapter(hours)

                    } else {
                        Toast.makeText(baseContext, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                } catch (e: HttpException) {
                    Toast.makeText(baseContext, "Exception: ${e.message()}", Toast.LENGTH_LONG).show()
                } catch (e: Throwable) {
                    Toast.makeText(baseContext, "Ooops: Something else went wrong", Toast.LENGTH_LONG).show()
                }
        })

        viewModel.getResponse(latitude, longitude)
       }


}
