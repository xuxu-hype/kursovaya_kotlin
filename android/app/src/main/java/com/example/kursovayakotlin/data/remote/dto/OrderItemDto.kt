package com.example.kursovayakotlin.data.remote.dto

data class OrderItemDto(
    val id: String,
    val orderId: String,
    val menuItemId: String?,
    val nameSnapshot: String,
    val priceCents: Int,
    val quantity: Int,
    val lineTotalCents: Int,
)
