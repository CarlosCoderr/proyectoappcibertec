package com.app.balance.repo

import com.app.balance.entity.Categoria
import com.app.balance.entity.TipoTransaccion

object CategoriaRepository {

    private val categoriasGasto = listOf(
        Categoria(1, "Alimentación", TipoTransaccion.GASTO),
        Categoria(2, "Transporte",  TipoTransaccion.GASTO),
        Categoria(3, "Servicios",   TipoTransaccion.GASTO),
        Categoria(4, "Salud",       TipoTransaccion.GASTO),
        Categoria(5, "Entretenimiento", TipoTransaccion.GASTO)
    )

    private val categoriasIngreso = listOf(
        Categoria(101, "Sueldo",    TipoTransaccion.INGRESO),
        Categoria(102, "Freelance", TipoTransaccion.INGRESO),
        Categoria(103, "Ventas",    TipoTransaccion.INGRESO),
        Categoria(104, "Otros",     TipoTransaccion.INGRESO)
    )

    fun listarPorTipo(tipo: TipoTransaccion): List<Categoria> =
        if (tipo == TipoTransaccion.GASTO) categoriasGasto else categoriasIngreso
}
