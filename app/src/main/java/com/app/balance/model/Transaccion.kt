package com.app.balance.model

<<<<<<< HEAD
<<<<<<< HEAD
=======
import java.time.LocalDate


>>>>>>> origin/main
=======
>>>>>>> 589dd31 (Desarollo de divisas, balance, Desarollo de header con menu, Dashboard fragment, gastos fragment, balance fragment, ademas de creacion de categoria Activity y transaccion gasto, persistencia de datos y CRUD completo,)
data class Transaccion(
    val id: Int = 0,
    val categoriaId: Int,
    val monto: Double,
    val fecha: String,
    val comentario: String? = null,
    val usuarioId: Int
)