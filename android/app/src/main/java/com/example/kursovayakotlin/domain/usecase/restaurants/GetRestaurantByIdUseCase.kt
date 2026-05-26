package com.example.kursovayakotlin.domain.usecase.restaurants

import com.example.kursovayakotlin.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetRestaurantByIdUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository,
) {
    suspend operator fun invoke(id: String) = restaurantRepository.getRestaurantById(id)
}
