package com.example.appmeteo.view

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appmeteo.R
import com.example.appmeteo.dao.WeatherCityDao
import com.example.appmeteo.database.CityRoomDatabase
import com.example.appmeteo.entity.WeatherCityEntity
import com.example.appmeteo.model.City
import com.example.appmeteo.repository.WeatherCityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch

class CityAdapter(private val items: List<City>, private val database: CityRoomDatabase) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    private val weatherCityDao: WeatherCityDao = database.cityDao()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val adressView: TextView = itemView.findViewById(R.id.textview_city_address)
        val cityView: TextView = itemView.findViewById(R.id.textview_city_city_code)
        val likeView: ImageView = itemView.findViewById(R.id.imageView_like)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println("je passe par la " + position)
        val item = items[position]
        holder.adressView.text = item.Address
        holder.cityView.text = item.Name + " " + item.Code



        holder.itemView.setOnClickListener {
            val context = holder.itemView.context as Activity
            val intent = Intent(context, WeatherActivity::class.java)
            intent.putExtra("latitude", item.Coordinates?.get(1).toString().toFloat())
            intent.putExtra("longitude", item.Coordinates?.get(0).toString().toFloat())
            intent.putExtra("city", item.Name)
            context.startActivity(intent)
        }

        holder.likeView.setOnClickListener {
            holder.likeView.setOnClickListener {
                val city = WeatherCityEntity(item.Id, item.Address, item.Name, item.Code,
                    item.Coordinates?.get(0) as Float, item.Coordinates?.get(1) as Float
                )
                if (city != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        weatherCityDao.insert(city)
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}