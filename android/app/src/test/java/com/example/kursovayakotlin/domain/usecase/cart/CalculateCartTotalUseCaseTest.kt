package com.example.kursovayakotlin.domain.usecase.cart

import com.example.kursovayakotlin.domain.model.CartItem
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateCartTotalUseCaseTest {
    private val useCase = CalculateCartTotalUseCase()

    @Test
    fun `calculates cart total in cents`() {
        val total = useCase(
            listOf(
                cartItem(menuItemId = "menu-1", priceCents = 1200, quantity = 2),
                cartItem(menuItemId = "menu-2", priceCents = 500, quantity = 3),
            ),
        )

        assertEquals(3900, total)
    }

    private fun cartItem(
        menuItemId: String,
        priceCents: Int,
        quantity: Int,
    ): CartItem =
        CartItem(
            menuItemId = menuItemId,
            restaurantId = "restaurant-1",
            name = menuItemId,
            priceCents = priceCents,
            imageUrl = null,
            quantity = quantity,
        )
}
