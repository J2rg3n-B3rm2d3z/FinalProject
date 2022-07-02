package com.laboratorios.finalproyect.views

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.laboratorios.finalproyect.views.Network.Callback
import com.laboratorios.finalproyect.views.Network.FirestoreService
import com.laboratorios.finalproyect.views.models.Cashier
import com.laboratorios.finalproyect.views.viewmodel.CashierViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AlertReceiver : BroadcastReceiver() {

    private lateinit var thisContext : Context
    private val _cashier = Cashier()
    private var listCashiers:ArrayList<Cashier> = ArrayList()

    private val firestoreService = FirestoreService()
    private var isLoading = true

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            thisContext = context
        }
        val cm = thisContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        if(isConnected)
            updateAllFromFirebase()

        Toast.makeText(thisContext,"Listo",Toast.LENGTH_LONG).show()
    }


    private fun updateAllFromFirebase() {
        firestoreService.getBacAtms(object: Callback<List<Cashier>> {

            override fun onSuccess(result: List<Cashier>?) {
                //listCashiers = result as ArrayList<Cashier>

                val current = LocalDateTime.now()//Fecha justo en el momento
                val formatter = DateTimeFormatter.ofPattern("E dd-MM HH:mm:ss")//Obtener el formato
                val formatted = current.format(formatter)//Obtener la fecha actual como String

                if (result != null) {
                    for (i in result.indices){
                        if(!result[i].money) {

                            result[i].money = true
                            result[i].date = formatted

                            _cashier.apply {
                                cashId = result[i].cashId
                                longitud = result[i].longitud
                                latitude = result[i].latitude
                                title = result[i].title
                                date = result[i].date
                                money = result[i].money
                            }

                            val cm = thisContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                            val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
                            if(isConnected)
                                updateBacStatus(_cashier)
                        }
                    }
                }

                processFinished()
            }

            // en caso de falle
            override fun onFailed(exception: Exception) {
                processFinished()
            }
        })
    }

    private fun updateBacStatus(cashier : Cashier){
        firestoreService.saveData(cashier)
    }

    //Finish process
    fun processFinished (){
        isLoading = false
        Toast.makeText(thisContext,"Todo ha sido Finalizado",Toast.LENGTH_LONG).show()
    }



/*
    private fun observeViewModel() {
        cashierViewModel._cashierList.observe(fragment, Observer<List<Cashier>>{
            listCashiers.clear()
            listCashiers.addAll(it)
        })

        cashierViewModel.isLoading.observe(fragment, Observer{
            if(it!=null){

                val current = LocalDateTime.now()//Fecha justo en el momento
                val formatter = DateTimeFormatter.ofPattern("E dd-MM HH:mm:ss")//Obtener el formato
                val formatted = current.format(formatter)//Obtener la fecha actual como String

                for (i in 0 until listCashiers.size){

                    if(!listCashiers[i].money) {

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

                        val cm = thisContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
                        if(isConnected)
                            cashierViewModel.updateData(_cashier)
                    }
                }

                Toast.makeText(thisContext,"Actualizacion hecha",Toast.LENGTH_LONG).show()
            }
        })
    }*/
}