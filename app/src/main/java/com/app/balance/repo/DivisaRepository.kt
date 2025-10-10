package com.app.balance.repo

import com.app.balance.entity.Divisa
import kotlinx.coroutines.delay

class DivisaRepository {

    // Lista estática de divisas (sin necesidad de API por ahora)
    private val divisasComunes = listOf(
        Divisa("EUR", "Euro", 0.85),
        Divisa("USD", "Dólar estadounidense", 1.0),
        Divisa("ARS", "Peso argentino", 350.0),
        Divisa("BRL", "Real brasileño", 4.95),
        Divisa("MXN", "Peso mexicano", 17.5),
        Divisa("CLP", "Peso chileno", 880.0),
        Divisa("PEN", "Sol peruano", 3.75),
        Divisa("COP", "Peso colombiano", 4200.0),
        Divisa("UYU", "Peso uruguayo", 39.0),
        Divisa("GBP", "Libra esterlina", 0.73),
        Divisa("JPY", "Yen japonés", 149.0),
        Divisa("CNY", "Yuan chino", 7.25),
        Divisa("CAD", "Dólar canadiense", 1.36),
        Divisa("AUD", "Dólar australiano", 1.52),
        Divisa("CHF", "Franco suizo", 0.88)
    )

    suspend fun obtenerDivisas(): Result<List<Divisa>> {
        return try {
            // Simular llamada a API con delay
            delay(500)
            Result.success(divisasComunes.sortedBy { it.nombre })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}