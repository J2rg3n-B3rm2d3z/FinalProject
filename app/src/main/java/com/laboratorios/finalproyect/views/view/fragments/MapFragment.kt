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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.laboratorios.finalproyect.R
import com.laboratorios.finalproyect.databinding.FragmentMapBinding
import com.laboratorios.finalproyect.views.data.InfoWindowData
import com.laboratorios.finalproyect.views.models.Cashier
import com.laboratorios.finalproyect.views.view.adapter.InfoWindowAdapter
import com.laboratorios.finalproyect.views.viewmodel.CashierViewModel
import java.time.LocalDateTime
import java.time.LocalTime
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

    private val _cashier = Cashier()

    private var infoWindowAdapter: InfoWindowAdapter ?= null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment

        _binding = FragmentMapBinding.inflate(inflater)
        val view = binding.root
        thisContext = view.context //GetContext

        //cashierViewModel.dailyWorkUpdate()

        //ViewModel

        cashierViewModel = ViewModelProviders.of(this)[CashierViewModel::class.java]
        cashierViewModel.refresh()

        observeViewModel()

        //validate if connected to internet

        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if(!isConnected)
            Toast.makeText(thisContext, "No puedes completar esta accion sin internet",
                Toast.LENGTH_LONG).show()

        return view
    }
    
    //Ver

    // observa si los datos han cambiado en la db para volver a cargarlos (creo que es eso)
    private fun observeViewModel() {

        cashierViewModel._cashierList.observe(viewLifecycleOwner, Observer<List<Cashier>>{
            listCashiers.clear()
            listCashiers.addAll(it)
        })

        cashierViewModel.isLoading.observe(viewLifecycleOwner, Observer{
            if(it!=null){
                val mapFragment =
                    childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
                mapFragment.getMapAsync(this)
                binding.mapFragmentLayout.visibility = View.VISIBLE
                binding.LoadingIcon.visibility = View.INVISIBLE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

   // aqui esta el override

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

        //googleMap.hideInfoWindow()

        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        //if connected to internet

        if(isConnected){

            val builder = MaterialAlertDialogBuilder(thisContext, R.style.MyThemeOverlay_MaterialAlertDialog)

                builder.setTitle("¿Está vacío?")
                    .setMessage("¿Actualizar estado?")
                    .setPositiveButton("Sí") {_, _ ->

                    for (i in listMarkerId) {//Para cada item en la clase creada en la parte inferior

                        if (i.marker == googleMap) {//Se compara le marcador para el marcador que se obtuvo como parametro

                            if (listCashiers[i.id].money) {//Si tiene dinero

                                //Configuracion del icono
                                val bitmapDraw = context?.applicationContext?.let {
                                    ContextCompat.getDrawable(
                                        it,
                                        R.drawable.ic_logo
                                    )
                                } as BitmapDrawable

                                //Obtener el momento actual/fecha actual
                                val current = LocalDateTime.now()//Fecha justo en el momento
                                val formatter = DateTimeFormatter.ofPattern("E hh:mm a")//Obtener el formato
                                val formatted = current.format(formatter)//Obtener la fecha actual como String


                                listCashiers[i.id].money = false//Se cambia el estado en la lista que se tiene en esta mismo fragment
                                listCashiers[i.id].date = formatted//Se cambia la fecha en la lista que se tiene en esta mismo fragment

                                //Aqui el codigo de para update firebase

                                // aquie tendria que ser capaz de otener
                                // el id del document jejje

                                // se guarda la informacion actual/actualizada del BAC

                                _cashier.apply {
                                    cashId = listCashiers[i.id].cashId
                                    latitude = listCashiers[i.id].latitude
                                    longitud = listCashiers[i.id].longitud
                                    title = listCashiers[i.id].title
                                    money = listCashiers[i.id].money
                                    date = listCashiers[i.id].date
                                }


                                // YA AQUI POR FIN PASO EL OBJETO O ESO ESPERO :v
                                cashierViewModel.updateData(_cashier)


                                //val snippet = _cashier.date + "Vacío"
                                val snippet = "Ult. actualización: $formatted \n Estado: Sin dinero"//Se cambia el texto del cuadro

                                val smallMarker =
                                    Bitmap.createScaledBitmap(bitmapDraw.bitmap,
                                        80, 80, false)


                                googleMap.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker))//Se actualiza el icono
                                googleMap.snippet = snippet //Se actualiza el snipel

                                var cashierStatus = "Sin dinero"

                                var info = InfoWindowData(
                                    listCashiers[i.id].title,
                                    cashierStatus,
                                    listCashiers[i.id].date
                                )

                                googleMap.tag = info
                                googleMap.showInfoWindow()

                            } else {
                                Toast.makeText(
                                    thisContext,
                                    "El cajero ya esta vacío",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            break
                        }
                    }
                }
                .setNegativeButton("No") { _, _ -> }
                .setCancelable(false)
                .show()
        }
        else{
            Toast.makeText(thisContext, "No puedes completar esta acción sin internet",
                Toast.LENGTH_LONG).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMapReady(googleMap: GoogleMap) {

        //Values Declaration

        //Obtener el momento actual/fecha actual
        val current = LocalDateTime.now()//Fecha justo en el momento
        val formatter = DateTimeFormatter.ofPattern("E hh:mm a")//Obtener el formato
        val formatted = current.format(formatter)//Obtener la fecha actual como String

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


        var promLatitude = 0.0
        var promLongitud = 0.0

        for (i in 0 until listCashiers.size){

            promLatitude += listCashiers[i].latitude
            promLongitud += listCashiers[i].longitud

        }

        val zoom = 13f
        //should modification Put a middle camera into all points

        val centerMap = LatLng(promLatitude/listCashiers.size, promLongitud/listCashiers.size)

        //Setup

        thisGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerMap, zoom))

        //for each cashier in the list

        for (i in 0 until listCashiers.size) {

            val centerMark = LatLng(listCashiers[i].latitude, listCashiers[i].longitud)
            val markerOptions = MarkerOptions()
            markerOptions.position(centerMark)
            markerOptions.title(listCashiers[i].title)

            //listCashiers[i].date = formatted

            var bitmapDraw:BitmapDrawable
            //var snippet:String
            var cashierStatus:String

            //Cambio pequeno
            //if the cashier have money

            if(listCashiers[i].money) {

                bitmapDraw = context?.applicationContext?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_logo_green
                    )
                } as BitmapDrawable

                cashierStatus = "Con dinero"

                //snippet = "Ult. actualización: " + listCashiers[i].date + "\n" + "Estado: Con dinero"

            }
            else{

                bitmapDraw = context?.applicationContext?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_logo
                    )
                } as BitmapDrawable

                cashierStatus = "Sin dinero"

                //snippet = "Ult. actualización: " + listCashiers[i].date + "\n" + "Estado: Sin dinero"
            }

            val smallMarker = Bitmap.createScaledBitmap(bitmapDraw.bitmap, 80,
                80, false)

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))//Set icon

            markerOptions.snippet("snippet")//this is the windows information

            var info = InfoWindowData(
                listCashiers[i].title,
                cashierStatus,
                listCashiers[i].date
            )

            var customInfoWindow = InfoWindowAdapter(thisContext)

            googleMap.setInfoWindowAdapter(customInfoWindow)

            val marker = googleMap.addMarker(markerOptions)
            marker?.let { MarkerAndId(it,i) } ?.let { listMarkerId.add(it) }
            //add the marker and the Id item-cashier in de listMarkerId to get a connexion

            marker?.tag = info

        }

        //Listeners
        thisGoogleMap.setOnMarkerClickListener(this)
        thisGoogleMap.setOnMyLocationClickListener(this)
        thisGoogleMap.setOnInfoWindowClickListener(this)
        //thisGoogleMap.setOnInfoWindowCloseListener(this)

    }
}

//Dataclass to use in the connection with Cashiers to Marker

data class MarkerAndId(val marker: Marker, val id: Int)