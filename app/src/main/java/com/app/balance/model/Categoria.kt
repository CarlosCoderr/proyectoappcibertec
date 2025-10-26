package com.app.balance.model

data class Categoria(
    val id: Int = 0,
    val nombre: String,
    val icono: String = "default",
    val usuarioId: Int,
    val tipoCategoriaId: Int,
<<<<<<< HEAD
<<<<<<< HEAD
    val rutaImagen: String? = null,
    val color: Int? = null
=======
    val rutaImagen: String? = null
>>>>>>> origin/main
=======
    val rutaImagen: String? = null,
    val color: Int? = null
>>>>>>> 589dd31 (Desarollo de divisas, balance, Desarollo de header con menu, Dashboard fragment, gastos fragment, balance fragment, ademas de creacion de categoria Activity y transaccion gasto, persistencia de datos y CRUD completo,)
)