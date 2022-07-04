package com.laboratorios.finalproyect.views.models
import java.io.*

class Cashier  (
    var latitude:Double=0.0,
    var longitud:Double=0.0,
    var title:String="",
    var date:String="",
    var money:Boolean=true,
    var cashId:String=""
) : Serializable {

    override fun toString(): String {
        return "$latitude $longitud $title $date $money $cashId"
    }
}
