package com.example.kursovayakotlin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val menuItemId: String,
    val restaurantId: String,
    val name: String,
    val priceCents: Int,
    val imageUrl: String?,
    val quantity: Int,
)
