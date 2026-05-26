package com.example.kursovayakotlin.domain.model

data class OrderItem(
    val id: String,
    val orderId: String,
    val menuItemId: String,
    val nameSnapshot: String,
    val priceCents: Int,
    val quantity: Int,
    val lineTotalCents: Int,
)
