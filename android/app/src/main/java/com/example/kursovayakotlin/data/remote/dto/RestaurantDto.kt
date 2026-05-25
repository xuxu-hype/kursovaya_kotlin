package com.example.kursovayakotlin.data.remote.dto

data class RestaurantDto(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val address: String?,
    val rating: Double,
    val isOpen: Boolean,
)
