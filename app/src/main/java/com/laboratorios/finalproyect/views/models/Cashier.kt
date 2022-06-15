package com.laboratorios.finalproyect.views.models

import java.io.Serializable

//Model to use to create a Cashier Object

class Cashier: Serializable{
    var latitude:Double=0.0
    var longitud:Double=0.0
    var title :String=""
    var date :String=""
    var money :Boolean=true
}


//data class Cashier(val Latitude: Double, val Longitude: Double,val Title:String, var Date:String, var Money:Boolean)
//Date si type String due to I dont know if json have a Date value so


//Ojito si volves aqui copia y pega lo de la profe
