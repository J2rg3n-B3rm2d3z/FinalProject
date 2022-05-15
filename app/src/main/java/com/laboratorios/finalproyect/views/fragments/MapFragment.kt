package com.laboratorios.finalproyect.views.fragments


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.laboratorios.finalproyect.R
import com.laboratorios.finalproyect.databinding.FragmentMapBinding
import com.laboratorios.finalproyect.views.models.Cashier
import java.util.*
import java.util.jar.Pack200
import kotlin.collections.ArrayList

class MapFragment : Fragment(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener{

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var thisContext : Context

    //to User Location values

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentMapBinding.inflate(inflater)
        val view = binding.root
        thisContext = view.context

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        //Values Declaration

        if (ActivityCompat.checkSelfPermission(
                thisContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                thisContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        googleMap.isMyLocationEnabled=true

        val zoom = 16f
        val centerMap = LatLng(getCashiers()[0].latitude, getCashiers()[0].longitude)
        //Se buscaria poner un punto medio entre todas las coordenadas

        //Setup

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerMap, zoom))

        for (i in 0 until getCashiers().size) {

            val centerMark = LatLng(getCashiers()[i].latitude, getCashiers()[i].longitude)
            val markerOptions = MarkerOptions()
            markerOptions.position(centerMark)
            markerOptions.title(getCashiers()[i].title)

            val bitmapDraw = context?.applicationContext?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.ic_localizacion
                )
            } as BitmapDrawable
            val smallMarker = Bitmap.createScaledBitmap(bitmapDraw.bitmap, 100, 100, false)
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

            val snippet = String.format(Locale.getDefault(), "Lat: %1$.5f, Long: %2$.5f",
                getCashiers()[i].latitude, getCashiers()[i].longitude)

            markerOptions.snippet(snippet)

            googleMap.addMarker(markerOptions)
        }

        googleMap.setOnMarkerClickListener(this)
        //---------------
        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.map_style
            )
        )

    }

    //When click in a marker

    override fun onMarkerClick(googleMap: Marker): Boolean {
        googleMap.showInfoWindow()

        return true
    }

    //Get a list of Cashier in the zone

    private fun getCashiers(): ArrayList<Cashier> {
        val listCashier: ArrayList<Cashier> = ArrayList()

        listCashier.add(Cashier(12.1318496, -86.2698217, "UNI-RUSB"))
        listCashier.add(Cashier(12.1035704, -86.2493089, "Galerias Santo Domingo"))
        listCashier.add(Cashier(12.136939, -86.2241076, "UNI-RUPAP"))
        listCashier.add(Cashier(12.1013463, -86.2959483, "Parque Nacional De Ferias"))

        return listCashier
    }


}