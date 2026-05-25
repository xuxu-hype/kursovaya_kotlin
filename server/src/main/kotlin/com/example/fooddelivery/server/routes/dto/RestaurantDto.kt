package com.example.fooddelivery.server.routes.dto

import com.example.fooddelivery.server.domain.model.Restaurant
import kotlinx.serialization.Serializable

@Serializable
data class RestaurantDto(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val address: String?,
    val rating: Double,
    val isOpen: Boolean,
)

fun Restaurant.toDto(): RestaurantDto =
    RestaurantDto(
        id = id.toString(),
        name = name,
        description = description,
        imageUrl = imageUrl,
        address = address,
        rating = rating.toDouble(),
        isOpen = isOpen,
    )
