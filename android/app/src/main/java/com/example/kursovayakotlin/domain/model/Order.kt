package com.example.kursovayakotlin.domain.model

data class Order(
    val id: String,
    val userId: String,
    val restaurantId: String,
    val status: OrderStatus,
    val totalCents: Int,
    val deliveryAddress: String,
    val createdAt: String,
    val updatedAt: String,
    val items: List<OrderItem>,
)
