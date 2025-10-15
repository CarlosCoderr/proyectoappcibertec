package com.app.balance.entity

import java.time.LocalDate


data class Transaccion(
    val id: Int,
    val monto: Double,
    val tipo: TipoTransaccion,
    val categoriaId: Int?,
    val categoriaNombre: String,
    val categoriaColor: Int?,
    val fecha: LocalDate,
    val comentario: String
)
