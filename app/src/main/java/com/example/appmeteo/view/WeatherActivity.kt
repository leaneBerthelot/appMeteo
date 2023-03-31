package com.example.appmeteo.view

import android.os.Bundle
import android.view.View
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
        val heureActuelle = LocalTime.now().hour

        val textview_temp_min_max = findViewById<TextView>(R.id.textview_temp_min_max)
        val textview_temp = findViewById<TextView>(R.id.textview_temp)
        val textview_code_temp = findViewById<TextView>(R.id.textview_code_temp)
        val textview_temp_res = findViewById<TextView>(R.id.textview_temp_res)


        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        viewModel.responseWeather.observe(this, Observer { response ->
                try {
                    if (response.isSuccessful) {
                        println("CALL API : ${response.body()}")

                        val tempMin = response.body()?.daily?.temperature_2m_min?.get(0)?.toString()
                        val tempMax = response.body()?.daily?.temperature_2m_max?.get(0)?.toString()
                        textview_temp_min_max.text = tempMin + "°C/" + tempMax + "°C"
                        textview_temp.text = response.body()?.hourly?.temperature_2m?.get(heureActuelle).toString()+ "°C"
                        textview_temp_res.text = response.body()?.hourly?.apparent_temperature?.get(heureActuelle).toString()+ "°C"
                        val weatherCode = response.body()?.hourly?.weathercode?.get(heureActuelle)
                        textview_code_temp.text = weatherCode?.let { viewModel.getDescription(it) }

                        val layoutManager = LinearLayoutManager(
                            this,
                            LinearLayoutManager.HORIZONTAL,
                            true
                        )

                        val myList = findViewById<View>(R.id.hour_recycler_view) as RecyclerView
                        myList.layoutManager = layoutManager

                        val days = mutableListOf<Prev>()
                        val size = response.body()?.daily?.temperature_2m_min?.size ?: 0

                        val calendar = Calendar.getInstance()
                        val day = calendar.get(Calendar.DAY_OF_WEEK)
                        //v

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

        viewModel.getResponse()

        // Database Room
        val applicationScope = CoroutineScope(SupervisorJob())
        val database = CityRoomDatabase.getDatabase(this, applicationScope)
        var repository = WeatherCityRepository(database.cityDao())

        }

}
