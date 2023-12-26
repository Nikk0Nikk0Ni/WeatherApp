package com.niko.weatherapp.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.niko.weatherapp.Adapters.WeatherAdapter
import com.niko.weatherapp.Model.MainViewModel
import com.niko.weatherapp.Model.WeatherItemModel
import com.niko.weatherapp.R
import com.niko.weatherapp.databinding.FragmentDaysBinding
import com.niko.weatherapp.databinding.FragmentHoursBinding
import org.json.JSONArray
import org.json.JSONObject

class HoursFragment : Fragment() {
    private lateinit var bind : FragmentHoursBinding
    private val adapter = WeatherAdapter(null)
    private val model : MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentHoursBinding.inflate(inflater)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecView()
        model.lifeDatacurrent.observe(viewLifecycleOwner){
            adapter.submitList(getHoursInfo(it))
        }
    }


    private fun initRecView()
    {
        bind.recViewHours.adapter = adapter
    }

    private fun getHoursInfo(item: WeatherItemModel) : List<WeatherItemModel>
    {
        val list = ArrayList<WeatherItemModel>()
        val hoursArray = JSONArray(item.hoursInfo)
        for(i in 0 until hoursArray.length())
        {
            val item = WeatherItemModel(
                "",
                (hoursArray[i] as JSONObject).getString("time"),
                (hoursArray[i] as JSONObject).getJSONObject("condition").getString("text"),
                "${(hoursArray[i] as JSONObject).getString("temp_c").toFloat().toInt()}°C",
                "",
                "",
                (hoursArray[i] as JSONObject).getJSONObject("condition").getString("icon"),
                ""
            )
            list.add(item)
        }
        return list
    }

    companion object {
        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}