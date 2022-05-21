package com.laboratorios.finalproyect.views.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laboratorios.finalproyect.views.models.Cashier

class CashierViewModel: ViewModel() {

    val cashierList = MutableLiveData<List<Cashier>>()

    fun getCashiers(){
        cashierList.postValue(getCashiersList())
    }

    //Get a list of Cashier in the zone
    //Change all this to get a the Database in Firestore

    private fun getCashiersList(): ArrayList<Cashier> {
        val listCashier: ArrayList<Cashier> = ArrayList()

        listCashier.add(Cashier(12.1318496, -86.2698217, "UNI-RUSB",true))
        listCashier.add(Cashier(12.1035704, -86.2493089, "Galerias Santo Domingo",true))
        listCashier.add(Cashier(12.136939, -86.2241076, "UNI-RUPAP",false))
        listCashier.add(Cashier(12.1013463, -86.2959483, "Parque Nacional De Ferias",true))

        return listCashier
    }


}