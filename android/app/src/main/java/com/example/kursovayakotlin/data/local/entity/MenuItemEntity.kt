package com.example.kursovayakotlin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_items")
data class MenuItemEntity(
    @PrimaryKey val id: String,
    val restaurantId: String,
    val name: String,
    val description: String?,
    val priceCents: Int,
    val imageUrl: String?,
    val isAvailable: Boolean,
)
