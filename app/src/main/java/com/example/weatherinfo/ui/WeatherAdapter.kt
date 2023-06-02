package com.example.weatherinfo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.weatherinfo.R
import com.example.weatherinfo.databinding.WeatherItemBinding
import com.example.weatherinfo.models.WeatherItem

class WeatherAdapter(private val context: Context) :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    var weatherInfoList: MutableList<WeatherItem> = mutableListOf()

    inner class ViewHolder(private val binding: WeatherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherItem: WeatherItem, context: Context) {
            binding.item = weatherItem
            val url = String.format(
                context.getString(R.string.icon_urls),
                weatherItem.weather.firstOrNull()?.icon
            )
            Glide.with(context)
                .load(url) // image url
                .override(200, 200) // resizing
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            WeatherItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(weatherInfoList[position], context)
    }

    override fun getItemCount(): Int {
        return weatherInfoList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: MutableList<WeatherItem>) {
        weatherInfoList = list
        notifyDataSetChanged()
    }
}