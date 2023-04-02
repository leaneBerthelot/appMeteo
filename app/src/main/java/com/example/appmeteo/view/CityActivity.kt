package com.example.appmeteo.view

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appmeteo.R
import com.example.appmeteo.database.CityRoomDatabase
import com.example.appmeteo.entity.WeatherCityEntity
import com.example.appmeteo.model.City
import com.example.appmeteo.model.Geometry
import com.example.appmeteo.repository.WeatherCityRepository
import com.example.appmeteo.viewModel.WeatherCityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import retrofit2.HttpException

internal class CityActivity : AppCompatActivity() {

    private lateinit var viewModel: WeatherCityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        val applicationScope = CoroutineScope(SupervisorJob())
        val database = CityRoomDatabase.getDatabase(this, applicationScope)
        val weatherCityDao = database.cityDao()


        val city = mutableListOf<City>()


        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        //list of fav city
        /*viewModel.getFavCity(weatherCityDao)

        viewModel.favoriteCities.observe(this, { favoriteCities ->
            city.clear()
            favoriteCities.forEach { cit ->
                city.add(
                    City(
                        cit.id,
                        cit.address,
                        cit.name,
                        cit.code,
                        Geometry(listOf(cit.latitude, cit.longitude))
                    )
                )
            }
            val cityRecyclerView = findViewById<RecyclerView>(R.id.city_recycler_view)
            cityRecyclerView.layoutManager = LinearLayoutManager(this)
            cityRecyclerView.adapter = CityAdapter(city, database)
        })*/

        val search_view = findViewById<SearchView>(R.id.search_view)

        viewModel = ViewModelProvider(this)[WeatherCityViewModel::class.java]

        viewModel.responseCity.observe(this, { response ->
            try {
                if (response.isSuccessful) {
                    println("CALL API : ${response.body()}")

                    val size = response.body()?.features?.size ?: 0

                    city.clear()

                    for (i in 0..size-1) {
                        city.add(City(
                            response.body()?.features?.get(i)?.properties?.id.toString(),
                            response.body()?.features?.get(i)?.properties?.name.toString(),
                            response.body()?.features?.get(i)?.properties?.city.toString(),
                            response.body()?.features?.get(i)?.properties?.postcode.toString(),
                            response.body()?.features?.get(i)?.geometry
                        ))
                    }

                    val cityRecyclerView = findViewById<RecyclerView>(R.id.city_recycler_view)
                    cityRecyclerView.layoutManager = LinearLayoutManager(this)
                    cityRecyclerView.adapter = CityAdapter(city, database)

                } else {
                    Toast.makeText(baseContext, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: HttpException) {
                Toast.makeText(baseContext, "Exception: ${e.message()}", Toast.LENGTH_LONG).show()
            } catch (e: Throwable) {
                Toast.makeText(baseContext, "Ooops: Something else went wrong", Toast.LENGTH_LONG).show()
            }
        })



        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getResponse(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        val test = WeatherCityRepository(weatherCityDao)

    }
}