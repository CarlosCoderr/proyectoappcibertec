package com.app.balance.network.apiClient

import com.app.balance.network.PaisesApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PaisesApiClient {
    private const val BASE_URL = "https://restcountries.com/v3.1/"

    fun crearServicio(): PaisesApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(PaisesApiService::class.java)
    }
}