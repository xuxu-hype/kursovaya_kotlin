package com.example.kursovayakotlin.domain.usecase.orders

import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.domain.repository.OrderRepository
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
) {
    suspend operator fun invoke(deliveryAddress: String) =
        if (deliveryAddress.isBlank()) {
            AppResult.Failure(code = "VALIDATION_ERROR", message = "Delivery address must not be blank.")
        } else {
            orderRepository.createOrder(deliveryAddress)
        }
}
