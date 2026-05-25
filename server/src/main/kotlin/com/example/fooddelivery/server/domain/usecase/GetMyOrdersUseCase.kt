package com.example.fooddelivery.server.domain.usecase

import com.example.fooddelivery.server.domain.model.Order
import com.example.fooddelivery.server.domain.repository.OrderRepository
import java.util.UUID

class GetMyOrdersUseCase(
    private val orderRepository: OrderRepository,
) {
    operator fun invoke(userId: UUID): List<Order> = orderRepository.findByUserId(userId)
}
