package com.laboratorios.finalproyect.views.models
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.*
import kotlinx.serialization.encodeToString

//Model to use to create a Cashier Object

// bueno amigos, lista para hacer chanchadas :b

class Cashier  (
    var latitude:Double=0.0,
    var longitud:Double=0.0,
    var title:String="",
    var date:String="",
    var money:Boolean=true,
    var cashId:String=""
) : Serializable {
    //The simplest way how you can hide sensitive information
    // is overriding toString
    override fun toString(): String {
        return "$latitude $longitud $title $date $money $cashId"
    }
}

/*class Cashier: Serializable{
    var latitude:Double=0.0
    var longitud:Double=0.0
    var title :String=""
    var date :String=""
    var money :Boolean=true
}*/


//data class Cashier(val Latitude: Double, val Longitude: Double,val Title:String, var Date:String, var Money:Boolean)
//Date si type String due to I dont know if json have a Date value so


//Ojito si volves aqui copia y pega lo de la profe
