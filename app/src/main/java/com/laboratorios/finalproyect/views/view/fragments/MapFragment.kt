package com.laboratorios.finalproyect.views.view.fragments


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.laboratorios.finalproyect.R
import com.laboratorios.finalproyect.databinding.FragmentMapBinding
import com.laboratorios.finalproyect.views.models.Cashier
import com.laboratorios.finalproyect.views.viewmodel.CashierViewModel
import java.util.*
import kotlin.collections.ArrayList

class MapFragment : Fragment(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener{

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    //Get Context and Google maps

    private lateinit var thisContext : Context
    private lateinit var gogleMap: GoogleMap

    //Get ViewModel and List

    private lateinit var cashierViewModel: CashierViewModel
    private val listCashiers:ArrayList<Cashier> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment

        _binding = FragmentMapBinding.inflate(inflater)
        val view = binding.root
        thisContext = view.context //GetContext

        //ViewModel

        cashierViewModel = ViewModelProviders.of(this)[CashierViewModel::class.java]
        cashierViewModel.getCashiers()
        cashierViewModel.cashierList.observe(viewLifecycleOwner){
            listCashiers.clear()
            listCashiers.addAll(it)
        }


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
        gogleMap = googleMap

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

        gogleMap.isMyLocationEnabled=true //Get Location

        val zoom = 16f
        val centerMap = LatLng(listCashiers[0].Latitude, listCashiers[0].Longitude)
        //Get a middle camera into all points

        //Setup

        gogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerMap, zoom))

        for (i in 0 until listCashiers.size) {

            val centerMark = LatLng(listCashiers[i].Latitude, listCashiers[i].Longitude)
            val markerOptions = MarkerOptions()
            markerOptions.position(centerMark)
            markerOptions.title(listCashiers[i].Title)

            val bitmapDraw = context?.applicationContext?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.ic_localizacion
                )
            } as BitmapDrawable
            val smallMarker = Bitmap.createScaledBitmap(bitmapDraw.bitmap, 100, 100, false)
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

            val snippet = String.format(Locale.getDefault(), "Lat: %1$.5f, Long: %2$.5f",
                listCashiers[i].Latitude, listCashiers[i].Longitude)

            markerOptions.snippet(snippet)

            googleMap.addMarker(markerOptions)
        }

        gogleMap.setOnMarkerClickListener(this)
        //---------------
        gogleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.map_style
            )
        )

    }

    //When click in a marker

    override fun onMarkerClick(googleMap: Marker): Boolean {
        val zoom = 13f
        gogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(googleMap.position,zoom))
        googleMap.showInfoWindow()
        return true
    }



}