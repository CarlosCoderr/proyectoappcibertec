package com.app.balance.model

<<<<<<< HEAD
=======
import java.time.LocalDate


>>>>>>> origin/main
data class Transaccion(
    val id: Int = 0,
    val categoriaId: Int,
    val monto: Double,
    val fecha: String,
    val comentario: String? = null,
    val usuarioId: Int
)