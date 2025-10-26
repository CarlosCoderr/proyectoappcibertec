package com.app.balance.model

data class Categoria(
    val id: Int = 0,
    val nombre: String,
    val icono: String = "default",
    val usuarioId: Int,
    val tipoCategoriaId: Int,
<<<<<<< HEAD
    val rutaImagen: String? = null,
    val color: Int? = null
=======
    val rutaImagen: String? = null
>>>>>>> origin/main
)