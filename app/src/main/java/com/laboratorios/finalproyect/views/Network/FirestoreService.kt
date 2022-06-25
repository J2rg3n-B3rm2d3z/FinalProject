package com.laboratorios.finalproyect.views.Network
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.laboratorios.finalproyect.views.models.Cashier

//The name has to be the same of the physical collection
const val BAC_ATMS="BAC_ATMS"
class FirestoreService {


    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()

    //private val dbRef = FirebaseDatabase.getInstance().getReference(BAC_ATMS)
    //private val _cashier = Cashier()

    // To get the data offline
    init{
        firebaseFirestore.firestoreSettings=settings
    }


    fun getBacAtms(callback: Callback <List<Cashier>>) {
        firebaseFirestore.collection(BAC_ATMS)
            .get()
            .addOnSuccessListener { result ->
                for(doc in result) {
                    // conver documentsList from db to class Cashier
                    val list=result.toObjects(Cashier::class.java)
                    callback.onSuccess(list)
                    break
                }
            }
    }

    // aqui solamente pasare el objeto, nada de listas!
    // fun to update data to db
    fun saveData(cashier: Cashier){
        // obtiene el doc en la db al que se dio click
        val document = firebaseFirestore.collection(BAC_ATMS).document(cashier.cashId)

        //cashier.cashId = document.id

        // AQUIII YA SE SUPONE QUE SE ACTUALIZA LA MIERDAAA
        val setData = document.set(cashier)
        setData.addOnSuccessListener {
            Log.d("Firebase", "document saved")
        }
        setData.addOnFailureListener {
            Log.d("Firebase", "Save Failed")
        }
    }
}