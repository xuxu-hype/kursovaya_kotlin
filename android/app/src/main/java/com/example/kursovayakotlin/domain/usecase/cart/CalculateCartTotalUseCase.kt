package com.example.kursovayakotlin.domain.usecase.cart

import com.example.kursovayakotlin.domain.model.CartItem
import javax.inject.Inject

class CalculateCartTotalUseCase @Inject constructor() {
    operator fun invoke(items: List<CartItem>): Int =
        items.sumOf { it.priceCents * it.quantity }
}
