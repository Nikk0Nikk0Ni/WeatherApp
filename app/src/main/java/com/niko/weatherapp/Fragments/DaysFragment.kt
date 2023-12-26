package com.niko.weatherapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.niko.weatherapp.Adapters.WeatherAdapter
import com.niko.weatherapp.Model.MainViewModel
import com.niko.weatherapp.Model.WeatherItemModel
import com.niko.weatherapp.databinding.FragmentDaysBinding


class DaysFragment : Fragment(), WeatherAdapter.Listener{
    private lateinit var bind : FragmentDaysBinding
    private val model : MainViewModel by activityViewModels()
    private val adapter = WeatherAdapter(this@DaysFragment )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bind = FragmentDaysBinding.inflate(inflater)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecView()
        model.lifeDataList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

    override fun onStart() {
        super.onStart()

    }

    private fun initRecView()
    {
        bind.recViewDays.adapter = adapter
    }

    override fun onCLick(item: WeatherItemModel) {
        model.lifeDatacurrent.value = item
    }

    companion object {
        @JvmStatic
        fun newInstance() = DaysFragment()
    }
}