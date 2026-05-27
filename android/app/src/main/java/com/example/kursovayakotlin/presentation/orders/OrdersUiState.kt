package com.example.kursovayakotlin.presentation.orders

import com.example.kursovayakotlin.domain.model.Order

data class OrdersUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
