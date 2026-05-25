package com.example.kursovayakotlin.data.remote.dto

data class CreateOrderRequestDto(
    val restaurantId: String,
    val items: List<CreateOrderItemRequestDto>,
    val deliveryAddress: String,
)
