package com.laboratorios.finalproyect.views

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.laboratorios.finalproyect.views.Network.Callback
import com.laboratorios.finalproyect.views.Network.FirestoreService
import com.laboratorios.finalproyect.views.models.Cashier
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AlertReceiver : BroadcastReceiver() {

    private lateinit var thisContext : Context
    private val _cashier = Cashier()

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

    }


    private fun updateAllFromFirebase() {
        firestoreService.getBacAtms(object: Callback<List<Cashier>> {

            override fun onSuccess(result: List<Cashier>?) {

                val current = LocalDateTime.now()//Fecha justo en el momento
                val formatter = DateTimeFormatter.ofPattern("E hh:mm a")//Obtener el formato
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
    }

}