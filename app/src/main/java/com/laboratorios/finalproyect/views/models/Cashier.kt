package com.laboratorios.finalproyect.views.models

import java.io.Serializable

//Model to use to create a Cashier Object

class Cashier: Serializable{
    var latitude = Double
    var longitude = Double
    var name = String
    var Date = String
    var money = Boolean
}


//data class Cashier(val Latitude: Double, val Longitude: Double,val Title:String, var Date:String, var Money:Boolean)
//Date si type String due to I dont know if json have a Date value so


//Ojito si volves aqui copia y pega lo de la profe
