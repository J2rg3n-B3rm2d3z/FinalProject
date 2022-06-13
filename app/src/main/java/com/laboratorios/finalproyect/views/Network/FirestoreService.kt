package com.laboratorios.finalproyect.views.Network
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.laboratorios.finalproyect.views.models.Cashier

//The name has to be the same of the physical collection
const val BAC_ATMS="BAC_ATMS"

class FirestoreService {
    val firebaseFirestore = FirebaseFirestore.getInstance()
    val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()

    // To get the data offline
    init{
        firebaseFirestore.firestoreSettings=settings
    }

    fun getBacAtms(callback: Callback <List<Cashier>>){
        firebaseFirestore.collection(BAC_ATMS).get()
            .addOnSuccessListener { result ->
                for(doc in result) {
                    val list=result.toObjects(Cashier::class.java)
                    callback.onSuccess(list)
                    break
                }
            }
            .addOnFailureListener { exception ->
                callback.onError(exception.message.toString())
            }
    }


}