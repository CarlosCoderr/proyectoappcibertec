package com.app.balance.repo

import com.app.balance.entity.Transaccion
import java.util.concurrent.atomic.AtomicInteger

object TransaccionRepository {

    // Generador de IDs automáticos
    private val autoId = AtomicInteger(0)

    // Lista en memoria de transacciones
    private val transacciones = mutableListOf<Transaccion>()

    /** Agrega una nueva transacción a la lista */
    fun agregar(transaccion: Transaccion) {
        val nueva = transaccion.copy(id = autoId.incrementAndGet())
        transacciones.add(nueva)
    }

    fun listar(): List<Transaccion> = transacciones.toList()

    fun listarPorTipo(tipo: String): List<Transaccion> =
        transacciones.filter { it.tipo.name == tipo }

    fun limpiar() {
        transacciones.clear()
        autoId.set(0)
    }
}
