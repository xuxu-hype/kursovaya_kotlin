package com.example.fooddelivery.server.domain.usecase

import com.example.fooddelivery.server.domain.model.Order
import com.example.fooddelivery.server.domain.repository.OrderRepository
import java.util.UUID

class GetOrderByIdUseCase(
    private val orderRepository: OrderRepository,
) {
    operator fun invoke(orderId: UUID, currentUserId: UUID): Order {
        val order = orderRepository.findById(orderId)
            ?: throw UseCaseError.NotFound("Order not found.")
        if (order.userId != currentUserId) {
            throw UseCaseError.NotFound("Order not found.")
        }
        return order
    }
}
