package com.laboratorios.finalproyect.views.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.laboratorios.finalproyect.views.Network.Callback
import com.laboratorios.finalproyect.views.Network.FirestoreService
import com.laboratorios.finalproyect.views.models.Cashier
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CashierViewModel: ViewModel() {

    val firestoreService = FirestoreService()
    //val listArtista : MutableLiveData<List<artista>> = MutableLiveData()
    val isLoading = MutableLiveData<Boolean>()
    val cashierList : MutableLiveData<List<Cashier>> = MutableLiveData()

    //Jurgen estuvo aqui

    //Refrescar

    fun refresh(){
        getScheduleFromFirebase()
    }

    //Obtener los datos

    private fun getScheduleFromFirebase() {
        firestoreService.getBacAtms(object: Callback<List<Cashier>>{
            override fun onSuccess(result: List<Cashier>?) {
                cashierList.postValue(result)
                processFinished()
            }

            override fun onFailed(exception: Exception) {
                processFinished()
            }
        })
    }

    //Finish process

    fun processFinished (){
        isLoading.value = true
    }

    //Aqui ya no estuvo Jurgen*/

    //getCashier in LiveData

    /*
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCashiers(){
        cashierList.postValue(getCashiersList())
    }

    //Get a list of Cashier in the zone
    //Change all this to get a the Database in Firestore


    @RequiresApi(Build.VERSION_CODES.O)
     fun getCashiersList(): ArrayList<Cashier> {
        val firestoreService = FirestoreService()
        val listCashier: ArrayList<Cashier> = ArrayList()
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("E dd-MM HH:mm:ss")
        val formatted = current.format(formatter)
        val isLoading= MutableLiveData<Boolean>()

        firestoreService.getBacAtms(object : Callback<List<Cashier>> {
            override fun onSuccess(result:List<Cashier>?){
                listCashier.addAll(result!!)
            }
            override fun onFailed(exception: Exception) {
                isLoading.value = true
            }
        })

        return listCashier
    }
       */

}