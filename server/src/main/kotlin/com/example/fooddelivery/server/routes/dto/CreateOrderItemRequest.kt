package com.example.fooddelivery.server.routes.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderItemRequest(
    val menuItemId: String,
    val quantity: Int,
)
