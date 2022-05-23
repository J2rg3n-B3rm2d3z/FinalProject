package com.laboratorios.finalproyect.views.models

//Model to use to create a Cashier Object

data class Cashier(val Latitude: Double, val Longitude: Double,val Title:String, var Date:String, var Money:Boolean)
//Date si type String due to I dont know if json have a Date value so