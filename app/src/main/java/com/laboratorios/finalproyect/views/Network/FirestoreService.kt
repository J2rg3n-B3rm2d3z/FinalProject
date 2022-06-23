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

    // creo que tengo que modificar esta funcion

    // con snapshot se pueden obtener actualizaciones
    // de la db en tiempo real
    /*fun getBacAtms(callback: Callback<List<Cashier>>) {
        firebaseFirestore.collection(BAC_ATMS).addSnapshotListener{ snapshot, e ->
                if (e != null){
                    Log.w(TAG, "Listen Failed", e)
                    return@addSnapshotListener
                }

                if (snapshot != null){

                    //val allCashiers = ArrayList<Cashier>()

                    // se obtienen todos los documentos de la coleccion
                    val documents = snapshot.documents

                    documents.forEach{

                        // se gurda a la clase cashier
                        val cashier = it.toObject(Cashier::class.java)

                        if (cashier != null){
                            // se obtiene cada uno de los id de todos los documentos
                            // para luego guardarlos en las listas
                            cashier.cashId = it.id

                            // i have no idea wtf i did here
                            //allCashiers.addAll(listOf(cashier))

                            //callback.onSuccess(allCashiers)
                            callback.onSuccess(listOf(cashier))
                        }
                    }
                }
            }
    }*/

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

    // funcion vieja
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

    // get atm based on id
    // obtiene la lista del documento buscado?
    /*fun getBacAtm(atmName :String, callback: Callback<List<CashierSerialize>>) {
        //FirebaseDatabase.getInstance().getReference(BAC_ATMS).child(id.toString())
        firebaseFirestore.collection(BAC_ATMS)
            .whereEqualTo("title", atmName)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    val atmInfo = documents.toObjects(CashierSerialize::class.java)
                    callback.onSuccess(atmInfo)
                    break
                }
            }
    }*/
}