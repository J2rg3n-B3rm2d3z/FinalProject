package com.laboratorios.finalproyect.views.Network

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.laboratorios.finalproyect.views.models.Cashier

const val BAC_ATMS="BAC_ATMS"
class FirestoreService {


    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()

    init{
        firebaseFirestore.firestoreSettings=settings
    }


    fun getBacAtms(callback: Callback <List<Cashier>>) {
        firebaseFirestore.collection(BAC_ATMS)
            .get()
            .addOnSuccessListener { result ->
                for(doc in result) {

                    val list=result.toObjects(Cashier::class.java)
                    callback.onSuccess(list)
                    break
                }
            }
    }

    fun saveData(cashier: Cashier){
        val document = firebaseFirestore.collection(BAC_ATMS).document(cashier.cashId)

        val setData = document.set(cashier)
        setData.addOnSuccessListener {
            Log.d("Firebase", "document saved")
        }
        setData.addOnFailureListener {
            Log.d("Firebase", "Save Failed")
        }
    }
}