package com.example.kursovayakotlin.domain.usecase.cart

import com.example.kursovayakotlin.domain.repository.CartRepository
import javax.inject.Inject

class RemoveFromCartUseCase @Inject constructor(
    private val cartRepository: CartRepository,
) {
    suspend operator fun invoke(menuItemId: String) = cartRepository.removeFromCart(menuItemId)
}
