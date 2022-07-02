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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.laboratorios.finalproyect.R
import com.laboratorios.finalproyect.databinding.FragmentMapBinding
import com.laboratorios.finalproyect.views.models.Cashier
import com.laboratorios.finalproyect.views.models.DailyWorker
import com.laboratorios.finalproyect.views.viewmodel.CashierViewModel
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
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


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment

        _binding = FragmentMapBinding.inflate(inflater)
        val view = binding.root
        thisContext = view.context //GetContext

        cashierViewModel.dailyWorkUpdate()

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        //if connected to internet

        if(isConnected){

            val builder = AlertDialog.Builder(thisContext)

            builder.setTitle("¿Esta vacio?")
                .setMessage("¿Estas seguro de cambiar el estado a vacio?")
                .setPositiveButton("Si") { _, _ ->

                    for (i in listMarkerId) {//Para cada item en la clase creada en la parte inferior

                        if (i.marker == googleMap) {//Se compara le marcador para el marcador que se obtuvo como parametro

                            if (listCashiers[i.id].money) {//Si tiene dinero

                                //Configuracion del icono

                                val bitmapDraw = context?.applicationContext?.let {
                                    ContextCompat.getDrawable(
                                        it,
                                        R.drawable.ic_localizacion_r
                                    )
                                } as BitmapDrawable

                                //Obtener el momento actual/fecha actual
                                val current = LocalDateTime.now()//Fecha justo en el momento
                                val formatter = DateTimeFormatter.ofPattern("E dd-MM HH:mm:ss")//Obtener el formato
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
                                val snippet = "$formatted Vacio"//Se cambia el texto del cuadro

                                val smallMarker =
                                    Bitmap.createScaledBitmap(bitmapDraw.bitmap,
                                        100, 100, false)

                                googleMap.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker))//Se actualiza el icono
                                googleMap.snippet = snippet//Se actualiza el snipel

                                /*Toast.makeText(thisContext, "Estado cambiado", Toast.LENGTH_LONG)
                                    .show()*/

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkHour() : Boolean {

        val status : Boolean

        // obtener hora actual
        val currentTime = LocalTime.now()

        if (currentTime.hour == 8) status = true
        else return false

        return status
    }

    //TODO
    // Probar esta onda manana

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMapReady(googleMap: GoogleMap) {

        //Values Declaration

        //Obtener el momento actual/fecha actual
        val current = LocalDateTime.now()//Fecha justo en el momento
        val formatter = DateTimeFormatter.ofPattern("E dd-MM HH:mm:ss")//Obtener el formato
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

        val zoom = 16f
        //should modification Put a middle camera into all points
        val centerMap = LatLng(listCashiers[0].latitude, listCashiers[0].longitud)

        //Setup

        thisGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerMap, zoom))

        //for each cashier in the list

        for (i in 0 until listCashiers.size) {

            val centerMark = LatLng(listCashiers[i].latitude, listCashiers[i].longitud)
            val markerOptions = MarkerOptions()
            markerOptions.position(centerMark)
            markerOptions.title(listCashiers[i].title)

            var bitmapDraw:BitmapDrawable
            var snippet:String

            //Cambio pequeno
            //if the cashier have money

            if(listCashiers[i].money) {

                bitmapDraw = context?.applicationContext?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_localizacion_g
                    )
                } as BitmapDrawable

                snippet = listCashiers[i].date + " No esta vacío"

            }
            else{

                //cashierViewModel.dailyWorkUpdate()

                // sino tiene dinero

                // si son las 8am
                /*if (checkHour()){

                    listCashiers[i].money = true
                    listCashiers[i].date = formatted

                    _cashier.apply {
                        cashId = listCashiers[i].cashId
                        longitud = listCashiers[i].longitud
                        latitude = listCashiers[i].latitude
                        title = listCashiers[i].title
                        date = listCashiers[i].date
                        money = listCashiers[i].money
                    }

                    // se envian los datos a la db actualizados
                    // solo se actualizan los atms que esten vacios
                    cashierViewModel.updateData(_cashier)
                }*/

                bitmapDraw = context?.applicationContext?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_localizacion_r
                    )
                } as BitmapDrawable

                snippet = listCashiers[i].date + " Vacío"
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

}

//Dataclass to use in the connection with Cashiers to Marker

data class MarkerAndId(val marker: Marker, val id: Int)