package com.sworks.ui.home.repository

import com.sworks.api.ApiService
import com.sworks.api.ResponseWrapper
import com.sworks.model.ClientList
import java.lang.Exception
import javax.inject.Inject

class HomeRepository @Inject constructor() {

    @Inject
    lateinit var apiService: ApiService

    suspend fun getClientList(): ResponseWrapper<ClientList> {

        return try {
            val response = apiService.getClientList()
            when {
                response.isSuccessful -> ResponseWrapper.success(response.body()!!)
                else -> {
                    if (response.errorBody() != null) {
                        ResponseWrapper.apiError(response.body(), response.errorBody()?.string())
                    } else {
                        ResponseWrapper.apiError(response.body(), response.message())
                    }
                }
            }
        } catch (e: Exception) {
            ResponseWrapper.httpError("${e.message}")
        }
    }
}