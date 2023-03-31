package com.example.appmeteo.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appmeteo.R
import com.example.appmeteo.model.Prev

class PrevAdapter(private val items: List<Prev>) : RecyclerView.Adapter<PrevAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayView: TextView = itemView.findViewById(R.id.textview_day)
        val MinMaxView: TextView = itemView.findViewById(R.id.textview_prev_temp_min_max)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.prevision, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        println("items value : $item")
        holder.dayView.text = item.Day
        holder.MinMaxView.text = item.MinMax
    }

    override fun getItemCount(): Int {
        println("items size : ${items.size}")
        return items.size
    }
}