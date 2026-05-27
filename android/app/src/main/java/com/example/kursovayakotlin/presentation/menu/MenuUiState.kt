package com.example.kursovayakotlin.presentation.menu

import com.example.kursovayakotlin.domain.model.MenuItem

data class MenuUiState(
    val restaurantId: String = "",
    val menuItems: List<MenuItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
