package com.laboratorios.finalproyect.views.Network

interface Callback {
    interface Callback<T> {
        fun onSuccess(result: T?)
        fun onFailed(exception: Exception)
    }
}