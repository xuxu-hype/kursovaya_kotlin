package com.example.kursovayakotlin.domain.usecase.restaurants

import com.example.kursovayakotlin.domain.repository.RestaurantRepository
import javax.inject.Inject

class RefreshMenuUseCase @Inject constructor(
    private val restaurantRepository: RestaurantRepository,
) {
    suspend operator fun invoke(restaurantId: String) = restaurantRepository.refreshMenu(restaurantId)
}
