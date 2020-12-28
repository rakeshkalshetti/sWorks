package com.sworks.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClientList(
    @Json(name = "orders")
    val orders: List<Order>
)

@JsonClass(generateAdapter = true)
data class Order(
    @Json(name = "name")
    val name: String,
    @Json(name = "location")
    val location: Location,
    @Json(name = "phone")
    val phone: Int,
    @Json(name = "address")
    val address: String
)

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "lat")
    val lat: Double,
    @Json(name = "long")
    val long: Double
)

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @field:Json(name = "status") var status: Int,
    @field:Json(name = "message") var message: String?,
    @field:Json(name = "result") var result: String?
)