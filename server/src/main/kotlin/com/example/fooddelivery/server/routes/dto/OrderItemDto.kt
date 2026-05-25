package com.example.fooddelivery.server.routes.dto

import com.example.fooddelivery.server.domain.model.OrderItem
import kotlinx.serialization.Serializable

@Serializable
data class OrderItemDto(
    val id: String,
    val orderId: String,
    val menuItemId: String?,
    val nameSnapshot: String,
    val priceCents: Int,
    val quantity: Int,
    val lineTotalCents: Int,
)

fun OrderItem.toDto(): OrderItemDto =
    OrderItemDto(
        id = id.toString(),
        orderId = orderId.toString(),
        menuItemId = menuItemId?.toString(),
        nameSnapshot = nameSnapshot,
        priceCents = priceCents,
        quantity = quantity,
        lineTotalCents = priceCents * quantity,
    )
