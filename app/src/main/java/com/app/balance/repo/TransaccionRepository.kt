package com.app.balance.repo

import com.app.balance.entity.Transaccion
import java.util.concurrent.atomic.AtomicInteger

object TransaccionRepository {
    private val autoId = AtomicInteger(0)
    private val transacciones = mutableListOf<Transaccion>()

    fun agregar(t: Transaccion) {
        transacciones.add(t.copy(id = autoId.incrementAndGet()))
    }

    fun listar(): List<Transaccion> = transacciones.toList()
}
