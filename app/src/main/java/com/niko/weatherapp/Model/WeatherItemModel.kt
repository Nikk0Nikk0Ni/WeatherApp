package com.niko.weatherapp.Model

data class WeatherItemModel(
    val city : String,
    val time : String,
    val condition : String,
    val currentTemp : String,
    val maxTemp : String,
    val minTemp : String,
    val imgLink : String,
    val hoursInfo : String
)
