package com.laboratorios.finalproyect.views.view.fragments


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.laboratorios.finalproyect.R
import com.laboratorios.finalproyect.databinding.FragmentMapBinding
import com.laboratorios.finalproyect.views.models.Cashier
import com.laboratorios.finalproyect.views.viewmodel.CashierViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class MapFragment : Fragment(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMyLocationClickListener,GoogleMap.OnInfoWindowClickListener{

    //Binding

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    //Get Context and Google maps

    private lateinit var thisContext : Context
    private lateinit var thisGoogleMap: GoogleMap

    //Get ViewModel

    private lateinit var cashierViewModel: CashierViewModel

    //Lists

    private val listMarkerId:ArrayList<MarkerAndId> = ArrayList()
    private val listCashiers:ArrayList<Cashier> = ArrayList()


    @RequiresApi(Build.VERSION_CODES.O)
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

        //validate if connected to internet

        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if(!isConnected)
            Toast.makeText(thisContext, "No puedes completar esta accion sin internet",
                Toast.LENGTH_LONG).show()

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

        thisGoogleMap = googleMap //get the item

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

        thisGoogleMap.isMyLocationEnabled=true //Put my ubication

        val zoom = 16f
        //should modification Put a middle camera into all points
        val centerMap = LatLng(listCashiers[0].Latitude, listCashiers[0].Longitude)

        //Setup

        thisGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerMap, zoom))

        //for each cashier in the list

        for (i in 0 until listCashiers.size) {

            val centerMark = LatLng(listCashiers[i].Latitude, listCashiers[i].Longitude)
            val markerOptions = MarkerOptions()
            markerOptions.position(centerMark)
            markerOptions.title(listCashiers[i].Title)

            var bitmapDraw:BitmapDrawable
            var snippet:String

            //if the cashier have money

            if(listCashiers[i].Money) {

                bitmapDraw = context?.applicationContext?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_localizacion_g
                    )
                } as BitmapDrawable

                snippet = listCashiers[i].Date + " No esta vacio"

            }
            else{

                bitmapDraw = context?.applicationContext?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_localizacion_r
                    )
                } as BitmapDrawable

                snippet = listCashiers[i].Date + " Vacio"
            }

            val smallMarker = Bitmap.createScaledBitmap(bitmapDraw.bitmap, 100,
                100, false)
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))//Set icon

            markerOptions.snippet(snippet)//this is the windows information

            googleMap.addMarker(markerOptions)?.let { MarkerAndId(it,i) } ?.let { listMarkerId.add(it) }
            //add the marker and the Id item-cashier in de listMarkerId to get a connexion

        }

        //Listeners
        thisGoogleMap.setOnMarkerClickListener(this)
        thisGoogleMap.setOnMyLocationClickListener(this)
        thisGoogleMap.setOnInfoWindowClickListener(this)

        //json Styles
        thisGoogleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.map_style
            )
        )

    }

    override fun onMarkerClick(googleMap: Marker): Boolean {
        //Show info windows and center the camera in the marker
            thisGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(googleMap.position))
            googleMap.showInfoWindow()

        return true
    }

    override fun onMyLocationClick(location: Location) {
        //Default Location
        Toast.makeText(thisContext,"Estas en ${location.latitude}, ${location.longitude}",
            Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onInfoWindowClick(googleMap: Marker) {

        //Change the state of Cashier to Empty

        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        //if connected to internet

        if(isConnected){

            val builder = AlertDialog.Builder(thisContext)
            builder.setTitle("¿Esta vacio?")
                .setMessage("¿Estas seguro de cambiar el estado a vacio?")
                .setPositiveButton("Si") { dialogInterface, it ->

                    for (i in listMarkerId) {

                        if (i.marker == googleMap) {

                            if (listCashiers[i.id].Money) {

                                val bitmapDraw = context?.applicationContext?.let {
                                    ContextCompat.getDrawable(
                                        it,
                                        R.drawable.ic_localizacion_r
                                    )
                                } as BitmapDrawable

                                val current = LocalDateTime.now()
                                val formatter = DateTimeFormatter.ofPattern("E dd-MM HH:mm:ss")
                                val formatted = current.format(formatter)

                                val snippet = "$formatted Vacio"

                                val smallMarker =
                                    Bitmap.createScaledBitmap(bitmapDraw.bitmap,
                                        100, 100, false)

                                googleMap.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                googleMap.snippet = snippet


                                listCashiers[i.id].Money = false
                                listCashiers[i.id].Date = formatted

                                Toast.makeText(thisContext, "Estado cambiado", Toast.LENGTH_LONG)
                                    .show()

                            } else {
                                Toast.makeText(
                                    thisContext,
                                    "El cajero ya esta vacio",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            break
                        }
                    }
                }
                .setNegativeButton("No") { dialogInterface, it -> }
                .setCancelable(false).show()
        }
        else{
            Toast.makeText(thisContext, "No puedes completar esta accion sin internet",
                Toast.LENGTH_LONG).show()
        }
    }
}

//Dataclass to use in the connection with Cashiers to Marker

data class MarkerAndId(val marker: Marker, val id: Int)