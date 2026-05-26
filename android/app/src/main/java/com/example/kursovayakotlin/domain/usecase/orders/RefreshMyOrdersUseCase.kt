package com.example.kursovayakotlin.domain.usecase.orders

import com.example.kursovayakotlin.domain.repository.OrderRepository
import javax.inject.Inject

class RefreshMyOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
) {
    suspend operator fun invoke() = orderRepository.refreshMyOrders()
}
