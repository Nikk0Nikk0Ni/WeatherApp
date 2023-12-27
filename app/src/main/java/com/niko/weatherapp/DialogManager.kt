package com.niko.weatherapp

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

object DialogManager {
    fun localSettingsDialog(context: Context, listenner: Listenner) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle("Enable Location?")
        dialog.setMessage("Location is disabled, do you want to enable it?")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { _, _ ->
            listenner.onOkClick()
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No") { _, _ ->
            listenner.onNoClick()
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    fun serachByCity(context : Context, onClick : (city : String?) -> Unit)
    {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog,null)
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setView(dialogView)
        val btn = dialogView.findViewById<Button>(R.id.btnOk)
        val edTxt = dialogView.findViewById<EditText>(R.id.cityName)
        btn.setOnClickListener{
            onClick(edTxt.text.toString())
            dialog.dismiss()
        }
        dialog.show()
    }

    interface Listenner {
        fun onOkClick()
        fun onNoClick()
    }
}