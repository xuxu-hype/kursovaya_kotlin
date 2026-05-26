package com.example.kursovayakotlin.domain.usecase.orders

import com.example.kursovayakotlin.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrderByIdUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
) {
    suspend operator fun invoke(orderId: String) = orderRepository.getOrderById(orderId)
}
