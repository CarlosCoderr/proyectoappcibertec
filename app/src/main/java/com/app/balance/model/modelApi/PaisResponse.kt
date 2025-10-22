package com.app.balance.model.modelApi

data class PaisResponse(
    val name: NameResponse,
    val idd: IddResponse,
    val flags: FlagsResponse,
    val cca2: String,
    val currencies: Map<String, Map<String, String>>? = null // NUEVO
)