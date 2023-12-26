package com.niko.weatherapp.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.niko.weatherapp.Model.WeatherItemModel
import com.niko.weatherapp.R
import com.niko.weatherapp.databinding.WeatherItemLayoutBinding
import com.squareup.picasso.Picasso


class WeatherAdapter(val listener : Listener?) : ListAdapter<WeatherItemModel, WeatherAdapter.WeatherHolder>(Comporator()) {
    class WeatherHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val binding = WeatherItemLayoutBinding.bind(view)
        fun bind(item : WeatherItemModel, listenner : Listener?){
            binding.apply {
                tvTemp.text = item.currentTemp.ifEmpty {"${item.minTemp}°C / ${item.maxTemp}°C"}
                tvDate.text = item.time
                tvConditition.text = item.condition
                Picasso.get().load("https:${item.imgLink}").into(imCondition)
                itemView.setOnClickListener{
                    listenner?.onCLick(item)
                }
            }
        }
    }

    class Comporator : DiffUtil.ItemCallback<WeatherItemModel>(){
        override fun areItemsTheSame(
            oldItem: WeatherItemModel,
            newItem: WeatherItemModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: WeatherItemModel,
            newItem: WeatherItemModel
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item_layout,parent,false)
        return WeatherHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        holder.bind(getItem(position),listener)
    }

    interface Listener{
        fun onCLick(item: WeatherItemModel)
    }
}