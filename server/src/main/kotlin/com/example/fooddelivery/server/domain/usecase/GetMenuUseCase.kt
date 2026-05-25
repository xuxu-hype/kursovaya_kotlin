package com.example.fooddelivery.server.domain.usecase

import com.example.fooddelivery.server.domain.model.MenuItem
import com.example.fooddelivery.server.domain.repository.RestaurantRepository
import java.util.UUID

class GetMenuUseCase(
    private val restaurantRepository: RestaurantRepository,
) {
    operator fun invoke(restaurantId: UUID): List<MenuItem> {
        restaurantRepository.findById(restaurantId)
            ?: throw UseCaseError.NotFound("Restaurant not found.")
        return restaurantRepository.findMenuByRestaurantId(restaurantId)
    }
}
