package com.example.kursovayakotlin.data.remote.dto

data class MenuItemDto(
    val id: String,
    val restaurantId: String?,
    val name: String,
    val description: String?,
    val priceCents: Int,
    val imageUrl: String?,
    val isAvailable: Boolean,
)
