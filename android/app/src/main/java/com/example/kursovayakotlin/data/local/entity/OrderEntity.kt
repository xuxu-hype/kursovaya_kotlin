package com.example.kursovayakotlin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val restaurantId: String,
    val status: String,
    val totalCents: Int,
    val deliveryAddress: String,
    val createdAt: String,
    val updatedAt: String,
)
