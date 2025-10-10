package com.app.balance.entity

data class Divisa(
    val codigo: String,
    val nombre: String,
    var tasaCambio: Double = 0.0
)