package com.app.balance.entity

enum class TipoCategoria { GASTO, INGRESO }

data class Categoria(
    val id: Int,
    val nombre: String,
    val color: Int?,
    val tipo: TipoCategoria
)
