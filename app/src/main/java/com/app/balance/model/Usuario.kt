package com.app.balance.model

data class Usuario(
    val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String,
    val genero: String,
    val celular: String,
    val email: String,
    val contrasena: String,
    val divisaId: Int,
<<<<<<< HEAD
<<<<<<< HEAD
    val montoTotal: Double = 0.0,
    val fotoPerfil: String? = null
)
=======
    val montoTotal: Double = 0.0
)
>>>>>>> origin/main
=======
    val montoTotal: Double = 0.0,
    val fotoPerfil: String? = null
)
>>>>>>> 589dd31 (Desarollo de divisas, balance, Desarollo de header con menu, Dashboard fragment, gastos fragment, balance fragment, ademas de creacion de categoria Activity y transaccion gasto, persistencia de datos y CRUD completo,)
