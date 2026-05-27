package com.example.kursovayakotlin.presentation.orders

import com.example.kursovayakotlin.domain.model.Order

data class OrderDetailsUiState(
    val orderId: String = "",
    val order: Order? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
