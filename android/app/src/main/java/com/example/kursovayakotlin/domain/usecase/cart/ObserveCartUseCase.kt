package com.example.kursovayakotlin.domain.usecase.cart

import com.example.kursovayakotlin.domain.repository.CartRepository
import javax.inject.Inject

class ObserveCartUseCase @Inject constructor(
    private val cartRepository: CartRepository,
) {
    operator fun invoke() = cartRepository.observeCart()
}
