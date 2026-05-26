package com.example.kursovayakotlin.domain.usecase.restaurants

import com.example.kursovayakotlin.domain.repository.RestaurantRepository
import javax.inject.Inject

class ObserveRestaurantsUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository,
) {
    operator fun invoke() = restaurantRepository.observeRestaurants()
}
