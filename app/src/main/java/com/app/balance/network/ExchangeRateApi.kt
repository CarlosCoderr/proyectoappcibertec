package com.app.balance.network

import retrofit2.Response
import retrofit2.http.GET

interface ExchangeRateApi {
    @GET("latest?base=USD")
    suspend fun getExchangeRates(): Response<ExchangeRateResponse>
}

data class ExchangeRateResponse(
    val rates: Map<String, Double>
)