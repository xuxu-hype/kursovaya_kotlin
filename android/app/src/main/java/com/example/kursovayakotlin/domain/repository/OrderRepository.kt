package com.example.kursovayakotlin.domain.repository

import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun createOrder(deliveryAddress: String): AppResult<Order>

    fun observeOrders(): Flow<List<Order>>

    suspend fun refreshMyOrders(): AppResult<Unit>

    suspend fun getOrderById(orderId: String): AppResult<Order>
}
