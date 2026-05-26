package com.example.kursovayakotlin.domain.usecase.restaurants

import com.example.kursovayakotlin.domain.repository.RestaurantRepository
import javax.inject.Inject

class ObserveMenuUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository,
) {
    operator fun invoke(restaurantId: String) = restaurantRepository.observeMenu(restaurantId)
}
