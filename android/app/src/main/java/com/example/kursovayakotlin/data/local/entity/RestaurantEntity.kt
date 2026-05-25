package com.example.kursovayakotlin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val address: String?,
    val rating: Double,
    val isOpen: Boolean,
)
