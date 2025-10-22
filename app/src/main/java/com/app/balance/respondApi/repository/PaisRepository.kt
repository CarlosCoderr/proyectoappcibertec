package com.app.balance.respondApi.repository

import com.app.balance.model.CountryCode
import com.app.balance.network.PaisesApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PaisRepository(private val paisService: PaisesApiService) {

    suspend fun cargarPaises(): Result<List<CountryCode>> = withContext(Dispatchers.IO) {
        try {
            val response = paisService.obtenerPaises()

            if (response.isSuccessful) {
                val paises = response.body() ?: emptyList()

                val paisesFormato = paises.mapNotNull { pais ->
                    val nombre = pais.name.common
                    val bandera = pais.flags.png ?: pais.flags.svg ?: ""

                    // Si no hay bandera, saltar
                    if (bandera.isEmpty()) return@mapNotNull null

                    // Obtener código de divisa desde la API
                    val codigoDivisa = obtenerCodigoDivisaDelPais(pais.currencies)

                    // Si no hay divisa, saltar
                    if (codigoDivisa.isEmpty()) return@mapNotNull null

                    CountryCode(
                        nombre = nombre,
                        codigo = codigoDivisa,
                        bandera = bandera
                    )
                }.sortedBy { it.nombre }
                    .distinctBy { it.codigo } // Eliminar duplicados por código de divisa

                Result.success(paisesFormato)
            } else {
                Result.failure(Exception("Error en la API: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Extrae el código de divisa del objeto currencies de la API
     * Estructura: { "USD": { "name": "United States dollar", "symbol": "$" } }
     */
    private fun obtenerCodigoDivisaDelPais(currencies: Map<String, Map<String, String>>?): String {
        if (currencies == null || currencies.isEmpty()) {
            return ""
        }

        // Obtener la primera divisa del mapa
        val primeraCodigo = currencies.keys.firstOrNull() ?: ""
        return primeraCodigo
    }
}