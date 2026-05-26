package com.example.kursovayakotlin.domain.model

data class CartItem(
    val menuItemId: String,
    val restaurantId: String,
    val name: String,
    val priceCents: Int,
    val imageUrl: String?,
    val quantity: Int,
)
