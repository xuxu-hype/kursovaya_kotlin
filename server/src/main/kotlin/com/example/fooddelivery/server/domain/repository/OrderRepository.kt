package com.example.fooddelivery.server.domain.repository

import com.example.fooddelivery.server.domain.model.Order
import com.example.fooddelivery.server.domain.model.OrderStatus
import java.util.UUID

data class NewOrderItem(
    val menuItemId: UUID,
    val nameSnapshot: String,
    val priceCents: Int,
    val quantity: Int,
)

interface OrderRepository {
    fun create(
        userId: UUID,
        restaurantId: UUID,
        status: OrderStatus,
        totalCents: Int,
        deliveryAddress: String,
        items: List<NewOrderItem>,
    ): Order

    fun findByUserId(userId: UUID): List<Order>

    fun findById(id: UUID): Order?
}
