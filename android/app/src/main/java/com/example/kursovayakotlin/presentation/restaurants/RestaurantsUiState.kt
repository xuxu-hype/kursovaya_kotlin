package com.example.kursovayakotlin.presentation.restaurants

import com.example.kursovayakotlin.domain.model.Restaurant

data class RestaurantsUiState(
    val restaurants: List<Restaurant> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
