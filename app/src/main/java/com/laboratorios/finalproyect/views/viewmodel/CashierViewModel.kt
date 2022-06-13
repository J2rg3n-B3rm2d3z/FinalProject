package com.laboratorios.finalproyect.views.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.laboratorios.finalproyect.views.models.Cashier
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CashierViewModel: ViewModel() {

    val cashierList = MutableLiveData<List<Cashier>>()

    //getCashier in LiveData

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCashiers(){
        cashierList.postValue(getCashiersList())
    }

    //Get a list of Cashier in the zone
    //Change all this to get a the Database in Firestore


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCashiersList(): ArrayList<Cashier> {
        val db = Firebase.firestore
        val listCashier: ArrayList<Cashier> = ArrayList()

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("E dd-MM HH:mm:ss")
        val formatted = current.format(formatter)

        db.collection("BAC_ATMS")
            .get()
            .addOnSuccessListener { result ->

            }

        /*listCashier.add(Cashier(12.1318496, -86.2698217, "UNI-RUSB",formatted,true))
        listCashier.add(Cashier(12.1035704, -86.2493089, "Galerias Santo Domingo",formatted,true))
        listCashier.add(Cashier(12.136939, -86.2241076, "UNI-RUPAP",formatted,true))
        listCashier.add(Cashier(12.1013463, -86.2959483, "Parque Nacional De Ferias",formatted,true))
        listCashier.add(Cashier(12.1117465,-86.2760271,"XCAPE",formatted,true))*/

        return listCashier
    }


}