package com.example.kursovayakotlin.presentation.cart

import com.example.kursovayakotlin.domain.model.CartItem

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val totalCents: Int = 0,
    val deliveryAddress: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val createdOrderId: String? = null,
)
