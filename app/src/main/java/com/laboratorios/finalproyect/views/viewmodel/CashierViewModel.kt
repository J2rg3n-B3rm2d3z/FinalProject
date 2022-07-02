package com.laboratorios.finalproyect.views.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.laboratorios.finalproyect.views.Network.Callback
import com.laboratorios.finalproyect.views.Network.FirestoreService
import com.laboratorios.finalproyect.views.models.Cashier
import com.laboratorios.finalproyect.views.models.DailyWorker
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit

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

    fun dailyWorkUpdate(){
        loadInteraction()
    }

    //Obtener los datos de la db
    private fun getScheduleFromFirebase() {
        firestoreService.getBacAtms(object: Callback<List<Cashier>>{

            // si los datos se cargaron
            override fun onSuccess(result: List<Cashier>?) {
                _cashierList.postValue(result)
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

    private fun loadInteraction(){
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        // establecer hora de ejecucion a las 8:00:00 AM
        dueDate.set(Calendar.HOUR_OF_DAY, 8)
        dueDate.set(Calendar.MINUTE, 0)
        dueDate.set(Calendar.SECOND, 0)

        // si la hora de ejecucion es antes la hora actual
        if (dueDate.before(currentDate)){
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

        val dailyWorkRequest = OneTimeWorkRequestBuilder<DailyWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance().enqueue(dailyWorkRequest)

        // update data at 8AM
        //cashierViewModel.updateData(cashier)
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