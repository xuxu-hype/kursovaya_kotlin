package com.example.kursovayakotlin.domain.model

data class Restaurant(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val address: String?,
    val rating: Double,
    val isOpen: Boolean,
)
