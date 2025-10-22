package com.app.balance.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ExchangeRateService {
    @GET("latest?base=USD")
    suspend fun obtenerDivisas(): retrofit2.Response<Map<String, Any>>
}

