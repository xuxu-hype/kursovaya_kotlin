package com.example.fooddelivery.server.routes.dto

import com.example.fooddelivery.server.domain.model.MenuItem
import kotlinx.serialization.Serializable

@Serializable
data class MenuItemDto(
    val id: String,
    val restaurantId: String?,
    val name: String,
    val description: String?,
    val priceCents: Int,
    val imageUrl: String?,
    val isAvailable: Boolean,
)

fun MenuItem.toDto(): MenuItemDto =
    MenuItemDto(
        id = id.toString(),
        restaurantId = restaurantId?.toString(),
        name = name,
        description = description,
        priceCents = priceCents,
        imageUrl = imageUrl,
        isAvailable = isAvailable,
    )
