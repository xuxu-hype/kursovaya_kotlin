package com.example.fooddelivery.server.domain.usecase

import com.example.fooddelivery.server.domain.model.Restaurant
import com.example.fooddelivery.server.domain.repository.RestaurantRepository
import java.util.UUID

class GetRestaurantByIdUseCase(
    private val restaurantRepository: RestaurantRepository,
) {
    operator fun invoke(id: UUID): Restaurant =
        restaurantRepository.findById(id)
            ?: throw UseCaseError.NotFound("Restaurant not found.")
}
