package com.example.fooddelivery.server.domain.usecase

import com.example.fooddelivery.server.domain.model.Restaurant
import com.example.fooddelivery.server.domain.repository.RestaurantRepository

class GetRestaurantsUseCase(
    private val restaurantRepository: RestaurantRepository,
) {
    operator fun invoke(): List<Restaurant> = restaurantRepository.findAll()
}
