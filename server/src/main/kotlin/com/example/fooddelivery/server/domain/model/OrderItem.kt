package com.example.fooddelivery.server.domain.model

import java.util.UUID

data class OrderItem(
    val id: UUID,
    val orderId: UUID,
    val menuItemId: UUID?,
    val nameSnapshot: String,
    val priceCents: Int,
    val quantity: Int,
)
