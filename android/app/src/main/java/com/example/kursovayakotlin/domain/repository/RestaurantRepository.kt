package com.example.kursovayakotlin.domain.repository

import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.domain.model.MenuItem
import com.example.kursovayakotlin.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {
    fun observeRestaurants(): Flow<List<Restaurant>>

    suspend fun refreshRestaurants(): AppResult<Unit>

    suspend fun getRestaurantById(id: String): AppResult<Restaurant>

    fun observeMenu(restaurantId: String): Flow<List<MenuItem>>

    suspend fun refreshMenu(restaurantId: String): AppResult<Unit>
}
