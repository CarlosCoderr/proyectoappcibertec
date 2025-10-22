package com.app.balance.network

import com.app.balance.model.modelApi.PaisResponse
import retrofit2.http.GET
import retrofit2.Response



interface PaisesApiService {
    @GET("all?fields=name,idd,cca2,flags,currencies")
    suspend fun obtenerPaises(): Response<List<PaisResponse>>
}

