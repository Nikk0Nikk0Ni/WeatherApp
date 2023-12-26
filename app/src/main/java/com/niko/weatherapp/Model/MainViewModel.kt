package com.niko.weatherapp.Model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.niko.weatherapp.Model.WeatherItemModel

class MainViewModel : ViewModel() {
    val lifeDatacurrent = MutableLiveData<WeatherItemModel>()
    val lifeDataList = MutableLiveData<List<WeatherItemModel>>()
}