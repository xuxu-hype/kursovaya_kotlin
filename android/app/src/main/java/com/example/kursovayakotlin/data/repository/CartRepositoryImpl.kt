package com.example.kursovayakotlin.data.repository

import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.data.local.dao.CartDao
import com.example.kursovayakotlin.data.mapper.toCartItemEntity
import com.example.kursovayakotlin.data.mapper.toDomain
import com.example.kursovayakotlin.domain.model.CartItem
import com.example.kursovayakotlin.domain.model.MenuItem
import com.example.kursovayakotlin.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
) : CartRepository {
    override fun observeCart(): Flow<List<CartItem>> =
        cartDao.observeCart().map { cart ->
            cart.map { it.toDomain() }
        }

    override suspend fun addToCart(menuItem: MenuItem): AppResult<Unit> =
        runCatching {
            val currentCart = cartDao.getCartOnce()
            val existingItem = currentCart.firstOrNull { it.menuItemId == menuItem.id }

            if (currentCart.any { it.restaurantId != menuItem.restaurantId }) {
                cartDao.clearCart()
            }

            val quantity = if (existingItem == null) 1 else existingItem.quantity + 1
            cartDao.upsertCartItem(menuItem.toCartItemEntity(quantity = quantity))
        }.fold(
            onSuccess = { AppResult.Success(Unit) },
            onFailure = { AppResult.Failure(message = it.message, cause = it) },
        )

    override suspend fun updateQuantity(menuItemId: String, quantity: Int): AppResult<Unit> =
        runCatching {
            if (quantity <= 0) {
                cartDao.deleteCartItem(menuItemId)
            } else {
                cartDao.updateQuantity(menuItemId, quantity)
            }
        }.fold(
            onSuccess = { AppResult.Success(Unit) },
            onFailure = { AppResult.Failure(message = it.message, cause = it) },
        )

    override suspend fun removeFromCart(menuItemId: String): AppResult<Unit> =
        runCatching {
            cartDao.deleteCartItem(menuItemId)
        }.fold(
            onSuccess = { AppResult.Success(Unit) },
            onFailure = { AppResult.Failure(message = it.message, cause = it) },
        )

    override suspend fun clearCart(): AppResult<Unit> =
        runCatching {
            cartDao.clearCart()
        }.fold(
            onSuccess = { AppResult.Success(Unit) },
            onFailure = { AppResult.Failure(message = it.message, cause = it) },
        )

    override suspend fun getCartOnce(): AppResult<List<CartItem>> =
        runCatching {
            cartDao.getCartOnce().map { it.toDomain() }
        }.fold(
            onSuccess = { AppResult.Success(it) },
            onFailure = { AppResult.Failure(message = it.message, cause = it) },
        )
}
