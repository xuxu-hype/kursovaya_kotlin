package com.example.fooddelivery.server.routes.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRequest(
    val restaurantId: String,
    val items: List<CreateOrderItemRequest>,
    val deliveryAddress: String,
)
