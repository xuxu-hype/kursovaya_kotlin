package com.example.kursovayakotlin.domain.model

data class MenuItem(
    val id: String,
    val restaurantId: String,
    val name: String,
    val description: String?,
    val priceCents: Int,
    val imageUrl: String?,
    val isAvailable: Boolean,
)
