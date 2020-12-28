package com.sworks.api

import android.text.TextUtils
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.sworks.model.ErrorResponse

enum class Status {
    SUCCESS,
    API_ERROR,
    HTTP_ERROR,
    LOADING
}

data class ResponseWrapper<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): ResponseWrapper<T> =
            ResponseWrapper(
                Status.SUCCESS,
                data,
                null
            )

        fun <T> apiError(data: T?, message: String?): ResponseWrapper<T> =
            ResponseWrapper(
                Status.API_ERROR,
                data,
                message
            )

        fun <T> httpError(message: String?): ResponseWrapper<T> =
            ResponseWrapper(
                Status.HTTP_ERROR,
                null,
                message
            )

        fun <T> loading(): ResponseWrapper<T> =
            ResponseWrapper(
                Status.LOADING,
                null,
                null
            )

        fun parseError(errorBody: String?): ErrorResponse? {
            if (TextUtils.isEmpty(errorBody)) {
                return null
            }

            val moshi = Moshi.Builder().build()
            val adapter: JsonAdapter<ErrorResponse> = moshi.adapter(ErrorResponse::class.java)
            return adapter.fromJson(errorBody!!)
        }
    }
}