package com.example.fooddelivery.server.routes.dto

import com.example.fooddelivery.server.domain.model.Order
import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val id: String,
    val userId: String?,
    val restaurantId: String?,
    val status: String,
    val totalCents: Int,
    val deliveryAddress: String,
    val createdAt: String,
    val updatedAt: String,
    val items: List<OrderItemDto>,
)

fun Order.toDto(): OrderDto =
    OrderDto(
        id = id.toString(),
        userId = userId?.toString(),
        restaurantId = restaurantId?.toString(),
        status = status.name,
        totalCents = totalCents,
        deliveryAddress = deliveryAddress,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString(),
        items = items.map { it.toDto() },
    )
