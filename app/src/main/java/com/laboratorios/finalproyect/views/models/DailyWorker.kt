package com.laboratorios.finalproyect.views.models

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.*
import com.google.firebase.firestore.FirebaseFirestore
import com.laboratorios.finalproyect.views.Network.BAC_ATMS
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class DailyWorker (context: Context, params : WorkerParameters) :
    Worker(context, params){

    // cash object
    private val _cashier = Cashier()

    // Data
    private val listCashiers:ArrayList<Cashier> = ArrayList()

    private val firebaseFirestore = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {

        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        val current = LocalDateTime.now()//Fecha justo en el momento
        val formatter = DateTimeFormatter.ofPattern("E dd-MM HH:mm:ss")//Obtener el formato
        val formatted = current.format(formatter)//Obtener la fecha actual como String

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

        WorkManager.getInstance()
            .enqueue(dailyWorkRequest)

        // leerlo dir desde la db
        firebaseFirestore.collection(BAC_ATMS)
            .whereEqualTo("money", false)
            .get()
            .addOnSuccessListener {
                documents ->

                val document = documents.documents

                document.forEach{

                    val cashier = it.toObject(Cashier::class.java)

                    if (cashier != null){
                        listCashiers.addAll(listOf(cashier))
                    }
                }
            }

        for(i in 0 until listCashiers.size){

            // lo unico que cambia son estos dos estados
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

            firebaseFirestore.collection(BAC_ATMS)
                .document(_cashier.cashId)
                .set(_cashier)
        }

        return Result.success()
    }
}