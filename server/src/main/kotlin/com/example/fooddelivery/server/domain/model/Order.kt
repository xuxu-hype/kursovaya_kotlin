package com.example.fooddelivery.server.domain.model

import java.time.Instant
import java.util.UUID

data class Order(
    val id: UUID,
    val userId: UUID?,
    val restaurantId: UUID?,
    val status: OrderStatus,
    val totalCents: Int,
    val deliveryAddress: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val items: List<OrderItem>,
)
