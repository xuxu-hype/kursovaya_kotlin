package com.example.kursovayakotlin.domain.usecase.cart

import com.example.kursovayakotlin.domain.model.MenuItem
import com.example.kursovayakotlin.domain.repository.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository,
) {
    suspend operator fun invoke(menuItem: MenuItem) = cartRepository.addToCart(menuItem)
}
