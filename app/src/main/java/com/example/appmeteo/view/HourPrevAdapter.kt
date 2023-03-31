package com.example.appmeteo.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appmeteo.R
import com.example.appmeteo.model.HourPrev


class HourPrevAdapter(private val items: List<HourPrev>) : RecyclerView.Adapter<HourPrevAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weatherCodeView: TextView = itemView.findViewById(R.id.textview_hour_code_temp)
        val tempView: TextView = itemView.findViewById(R.id.textview_hour_temp_res)
        val hourView: TextView = itemView.findViewById(R.id.textview_hour_hour)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hour_prev, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        println("items value : $item")
        holder.weatherCodeView.text = item.WeatherCode
        holder.tempView.text = item.Temp
        holder.hourView.text = item.Hour
    }

    override fun getItemCount(): Int {
        println("items size : ${items.size}")
        return items.size
    }
}