package com.example.kursovayakotlin.data.remote.dto

data class CreateOrderItemRequestDto(
    val menuItemId: String,
    val quantity: Int,
)
