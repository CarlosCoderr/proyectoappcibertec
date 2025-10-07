package com.app.balance.enity

data class Usuario(
    var codigo :Int,
    var nombre :String = "",
    var apellidos :String = "",
    var correo : String = "",
    var fechaNacimiento :String = "",
    var genero :String = "",
    var celular :String = "",
    var clave :String = "",

)