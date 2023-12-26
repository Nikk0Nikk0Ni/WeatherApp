package com.niko.weatherapp

import android.app.VoiceInteractor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.niko.weatherapp.databinding.ActivityMainBinding
import com.android.volley.Request
import com.niko.weatherapp.Fragments.MainFragment
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var bind: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainScreen,MainFragment.newInstance())
            .commit()
    }
}