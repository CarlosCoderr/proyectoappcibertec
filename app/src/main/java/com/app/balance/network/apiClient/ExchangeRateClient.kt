package com.app.balance.network.apiClient

import com.app.balance.network.ExchangeRateService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExchangeRateClient {
    companion object {
        private const val BASE_URL = "https://api.exchangerate-api.com/v4/"

        fun crearServicio(): ExchangeRateService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ExchangeRateService::class.java)
        }
    }
}