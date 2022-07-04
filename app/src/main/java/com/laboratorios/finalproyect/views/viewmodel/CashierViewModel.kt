package com.laboratorios.finalproyect.views.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laboratorios.finalproyect.views.Network.Callback
import com.laboratorios.finalproyect.views.Network.FirestoreService
import com.laboratorios.finalproyect.views.models.Cashier


class CashierViewModel: ViewModel() {

    private val firestoreService = FirestoreService()
    val isLoading = MutableLiveData<Boolean>()
    //val cashierList : MutableLiveData<List<Cashier>> = MutableLiveData()

    var _cashierList : MutableLiveData<List<Cashier>> = MutableLiveData()

    //Jurgen estuvo aqui

    //Refrescar

    fun refresh(){
        getScheduleFromFirebase()
    }

    fun updateData(cashier: Cashier){
        updateBacStatus(cashier)
    }

    //Obtener los datos de la db
    private fun getScheduleFromFirebase() {
        firestoreService.getBacAtms(object: Callback<List<Cashier>>{

            // si los datos se cargaron
            override fun onSuccess(result: List<Cashier>?) {
                _cashierList.postValue(result!!)
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
        isLoading.value = true
    }
}