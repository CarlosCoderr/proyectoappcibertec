package com.app.balance.respondApi.repository

import com.app.balance.data.dao.CountryCodeDAO
import com.app.balance.model.CountryCode
import com.app.balance.network.ExchangeRateService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DivisaRepository(
    private val divisaService: ExchangeRateService,
    private val divisaDAO: CountryCodeDAO
) {

    suspend fun cargarDivisasDesdeAPI(): Result<List<CountryCode>> = withContext(Dispatchers.IO) {
        try {
            val response = divisaService.obtenerDivisas()
            if (response.isSuccessful) {
                val body = response.body()
                val rates = body?.get("rates") as? Map<*, *>

                if (rates != null) {
                    val divisas = mutableListOf<CountryCode>()
                    var id = 1

                    rates.forEach { (codigo, _) ->
                        divisas.add(
                            CountryCode(
                                nombre = obtenerNombreDivisa(codigo.toString()),
                                codigo = codigo.toString()
                            )
                        )
                        id++
                    }

                    // Guardar en BD local
                    val guardadas = divisaDAO.insertarDivisas(divisas)
                    if (guardadas) {
                        Result.success(divisas)
                    } else {
                        Result.failure(Exception("Error al guardar divisas en BD local"))
                    }
                } else {
                    Result.failure(Exception("Formato de respuesta inválido"))
                }
            } else {
                Result.failure(Exception("Error en la API: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun obtenerDivisasLocales(): List<CountryCode> {
        return divisaDAO.obtenerTodosPaises()
    }

    fun obtenerDivisaPorCodigo(codigo: String): CountryCode? {
        return divisaDAO.obtenerPaisPorCodigo(codigo)
    }

    private fun obtenerNombreDivisa(codigo: String): String {
        return when (codigo) {
            "USD" -> "Dólar estadounidense"
            "EUR" -> "Euro"
            "ARS" -> "Peso argentino"
            "BRL" -> "Real brasileño"
            "MXN" -> "Peso mexicano"
            "CLP" -> "Peso chileno"
            "PEN" -> "Sol peruano"
            "COP" -> "Peso colombiano"
            "UYU" -> "Peso uruguayo"
            "VES" -> "Bolívar venezolano"
            else -> codigo
        }
    }
}