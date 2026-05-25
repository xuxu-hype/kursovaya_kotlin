package com.example.fooddelivery.server.domain.model

import java.util.UUID

data class MenuItem(
    val id: UUID,
    val restaurantId: UUID?,
    val name: String,
    val description: String?,
    val priceCents: Int,
    val imageUrl: String?,
    val isAvailable: Boolean,
)
