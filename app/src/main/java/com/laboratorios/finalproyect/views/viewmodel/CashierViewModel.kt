package com.laboratorios.finalproyect.views.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laboratorios.finalproyect.views.Network.Callback
import com.laboratorios.finalproyect.views.Network.FirestoreService
import com.laboratorios.finalproyect.views.models.Cashier


class CashierViewModel: ViewModel() {

    private val firestoreService = FirestoreService()
    val isLoading = MutableLiveData<Boolean>()


    var _cashierList : MutableLiveData<List<Cashier>> = MutableLiveData()

    fun refresh(){
        getScheduleFromFirebase()
    }

    fun updateData(cashier: Cashier){
        updateBacStatus(cashier)
    }


    private fun getScheduleFromFirebase() {
        firestoreService.getBacAtms(object: Callback<List<Cashier>>{

            override fun onSuccess(result: List<Cashier>?) {
                _cashierList.postValue(result!!)
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

    fun processFinished (){
        isLoading.value = true
    }
}