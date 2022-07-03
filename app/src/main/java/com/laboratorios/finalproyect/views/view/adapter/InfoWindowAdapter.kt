package com.laboratorios.finalproyect.views.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.compose.ui.layout.Layout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.laboratorios.finalproyect.databinding.InfoWindowBinding
import com.laboratorios.finalproyect.views.InfoWindowData
import kotlin.math.min

class InfoWindowAdapter(context: Context) : GoogleMap.InfoWindowAdapter {

    //private lateinit var binding : InfoWindowBinding

    private var binding = InfoWindowBinding.inflate(
        LayoutInflater.from(context), null, false
    )

    override fun getInfoContents(marker: Marker): View? {

        var mInfoWindow: InfoWindowData = marker.tag as InfoWindowData

        var status = mInfoWindow?.status

        binding.txtCashierName.text = mInfoWindow?.title

        if (status == "Con dinero"){
            binding.txtCashierStatus.setTextColor(Color.parseColor("#00FF00"))
        }
        else{
            binding.txtCashierStatus.setTextColor(Color.parseColor("#ff0000"))
        }

        binding.txtCashierStatus.text = status

        binding.txtLastUpdate.text = mInfoWindow?.lastUpdate

        return binding.root
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

}