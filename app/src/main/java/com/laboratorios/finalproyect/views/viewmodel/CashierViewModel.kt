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

    // private para encapsularlas
    var _cashierList : MutableLiveData<List<Cashier>> = MutableLiveData()

    //var _cashier = Cashier()

    //val serializeCasherList : MutableLiveData<List<CashierSerialize>> = MutableLiveData()

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
                // guarda todos los datos obtenidos del query en la lista
                // que es de tipo mutable

                // aqui tambien se guardaran cada uno de los ids obtenidos
                // de cada coleccion
                // se guardan los datos en la lista de tipo mutable
                _cashierList.postValue(result)

                processFinished()
            }

            // en caso de falle
            override fun onFailed(exception: Exception) {
                processFinished()
            }
        })
    }

    // creo que aqui tendria que hacer la funcion update
    // enviar mensaje de TO DO BIEN

    private fun updateBacStatus(cashier : Cashier){
        firestoreService.saveData(cashier)
        //val saveData = firestoreService.saveData(callback)
        //saveData.addO
    }

    /*private fun updateBacStatus(
        atmLat : Double,
        atmLon : Double,
        atmName : String,
        bacStatus: Boolean,
        bacCurrentDate:String,
        atmInt: String
    ){

    }*/


    //Finish process

    fun processFinished (){
        isLoading.value = true
    }

    // aqui se inicializan para poder ser utilziadas
    // y no acceder directamente a la variable
    /*internal var cashierList:MutableLiveData<List<Cashier>>
        get() {return _cashierList}
        set(value) {_cashierList.value}
       */




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