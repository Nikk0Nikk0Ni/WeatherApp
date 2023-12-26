package com.niko.weatherapp.Fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.tabs.TabLayoutMediator
import com.niko.weatherapp.Adapters.ViewPageAdapter
import com.niko.weatherapp.CONSTANCE
import com.niko.weatherapp.Model.MainViewModel
import com.niko.weatherapp.Model.WeatherItemModel
import com.niko.weatherapp.R
import com.niko.weatherapp.databinding.AlertDialogBinding
import com.niko.weatherapp.databinding.FragmentMainBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainFragment : Fragment() {
    private lateinit var fLocationClient: FusedLocationProviderClient
    private val flist = listOf(HoursFragment.newInstance(), DaysFragment.newInstance())
    private lateinit var bind: FragmentMainBinding
    private lateinit var plauncher: ActivityResultLauncher<String>
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentMainBinding.inflate(inflater)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        init()
        updateCurrentCard()

    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            requestWeatherData("Moscow")
        }

    }

    private fun permissionLauncher() {
        plauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            Toast.makeText(activity, "Permission is $it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermission() {
        if (!isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionLauncher()
            plauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

    }

    private fun init() = with(bind) {
        fLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val tabnamelist =
            listOf(resources.getString(R.string.hours), resources.getString(R.string.days))
        val adapter = ViewPageAdapter(activity as FragmentActivity, flist)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabnamelist[position]
        }.attach()
        btnSynch.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                requestWeatherData("Moscow")
            }
        }
    }

    private fun updateCurrentCard() = with(bind)
    {
        model.lifeDatacurrent.observe(activity as LifecycleOwner) {
            tvData.text = it.time
            tvCity.text = it.city
            tvCurrentTemp.text = it.currentTemp.ifEmpty { "${it.minTemp}/${it.maxTemp}" }
            tvCondition.text = it.condition
            tvMaxMin.text = if (it.currentTemp.isEmpty()) "" else "${it.minTemp}/${it.maxTemp}"
            Picasso.get().load("https:${it.imgLink}").into(imageWeather)
        }
    }

    private fun requestWeatherData(city: String) {
        val url =
            "https://api.weatherapi.com/v1/forecast.json?key=${CONSTANCE.API_KEY}&q=$city&days=3&aqi=no&alerts=no"
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result ->
                parseWeatherData(result)

            },
            { error ->

            }
        )
        queue.add(request)
    }

    private fun parseWeatherData(result: String) {
        val mainObj = JSONObject(result)
        val list = parseDays(mainObj)
        parseCurrentData(mainObj, list[0])
    }


    private fun parseCurrentData(mainObj: JSONObject, weatherItem: WeatherItemModel) {
        val item = WeatherItemModel(
            mainObj.getJSONObject("location").getString("name"),
            mainObj.getJSONObject("current").getString("last_updated"),
            mainObj.getJSONObject("current").getJSONObject("condition").getString("text"),
            "${mainObj.getJSONObject("current").getString("temp_c").toFloat().toInt()}°C",
            "${weatherItem.maxTemp}°C",
            "${weatherItem.minTemp}°C",
            mainObj.getJSONObject("current").getJSONObject("condition").getString("icon"),
            weatherItem.hoursInfo
        )
        model.lifeDatacurrent.value = item
    }

    private fun parseDays(mainObj: JSONObject): List<WeatherItemModel> {
        val list = ArrayList<WeatherItemModel>()
        val daysArray = mainObj.getJSONObject("forecast")
            .getJSONArray("forecastday")
        val name = mainObj.getJSONObject("location").getString("name")
        for (i in 0 until daysArray.length()) {
            val day = daysArray[i] as JSONObject
            val item = WeatherItemModel(
                name,
                day.getString("date"),
                day.getJSONObject("day").getJSONObject("condition").getString("text"),
                "",
                day.getJSONObject("day").getString("maxtemp_c").toFloat().toInt().toString(),
                day.getJSONObject("day").getString("mintemp_c").toFloat().toInt().toString(),
                day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                day.getJSONArray("hour").toString()
            )
            list.add(item)
        }
        model.lifeDataList.value = list
        return list
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}