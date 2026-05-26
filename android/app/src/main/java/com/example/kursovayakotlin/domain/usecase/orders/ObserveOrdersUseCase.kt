package com.example.kursovayakotlin.domain.usecase.orders

import com.example.kursovayakotlin.domain.repository.OrderRepository
import javax.inject.Inject

class ObserveOrdersUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
) {
    operator fun invoke() = orderRepository.observeOrders()
}
