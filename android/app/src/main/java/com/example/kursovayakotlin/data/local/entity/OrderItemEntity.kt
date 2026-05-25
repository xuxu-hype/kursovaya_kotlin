package com.example.kursovayakotlin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_items")
data class OrderItemEntity(
    @PrimaryKey val id: String,
    val orderId: String,
    val menuItemId: String,
    val nameSnapshot: String,
    val priceCents: Int,
    val quantity: Int,
    val lineTotalCents: Int,
)
