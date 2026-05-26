package com.example.kursovayakotlin.domain.repository

import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.domain.model.CartItem
import com.example.kursovayakotlin.domain.model.MenuItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeCart(): Flow<List<CartItem>>

    suspend fun addToCart(menuItem: MenuItem): AppResult<Unit>

    suspend fun updateQuantity(menuItemId: String, quantity: Int): AppResult<Unit>

    suspend fun removeFromCart(menuItemId: String): AppResult<Unit>

    suspend fun clearCart(): AppResult<Unit>

    suspend fun getCartOnce(): AppResult<List<CartItem>>
}
