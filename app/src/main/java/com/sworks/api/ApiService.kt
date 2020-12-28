package com.sworks.api

import com.sworks.model.ClientList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    @GET("clients")
    suspend fun getClientList(): Response<ClientList>
}