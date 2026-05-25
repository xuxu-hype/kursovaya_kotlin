package com.example.fooddelivery.server.domain.repository

import com.example.fooddelivery.server.domain.model.MenuItem
import com.example.fooddelivery.server.domain.model.Restaurant
import java.util.UUID

interface RestaurantRepository {
    fun findAll(): List<Restaurant>

    fun findById(id: UUID): Restaurant?

    fun findMenuByRestaurantId(restaurantId: UUID): List<MenuItem>

    fun findMenuItemById(id: UUID): MenuItem?
}
