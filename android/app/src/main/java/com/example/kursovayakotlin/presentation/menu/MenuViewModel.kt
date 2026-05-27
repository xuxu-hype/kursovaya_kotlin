package com.example.kursovayakotlin.presentation.menu

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.domain.model.MenuItem
import com.example.kursovayakotlin.domain.usecase.cart.AddToCartUseCase
import com.example.kursovayakotlin.domain.usecase.restaurants.ObserveMenuUseCase
import com.example.kursovayakotlin.domain.usecase.restaurants.RefreshMenuUseCase
import com.example.kursovayakotlin.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MenuViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeMenuUseCase: ObserveMenuUseCase,
    private val refreshMenuUseCase: RefreshMenuUseCase,
    private val addToCartUseCase: AddToCartUseCase,
) : ViewModel() {
    private val restaurantId: String = checkNotNull(savedStateHandle[Screen.Menu.RESTAURANT_ID])

    private val _uiState = MutableStateFlow(MenuUiState(restaurantId = restaurantId, isLoading = true))
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeMenuUseCase(restaurantId).collect { menuItems ->
                _uiState.update { it.copy(menuItems = menuItems) }
            }
        }
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = refreshMenuUseCase(restaurantId)) {
                is AppResult.Success -> _uiState.update { it.copy(isLoading = false) }
                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message ?: "Could not load menu.")
                    }
                }
            }
        }
    }

    fun addToCart(menuItem: MenuItem) {
        viewModelScope.launch {
            when (val result = addToCartUseCase(menuItem)) {
                is AppResult.Success -> _uiState.update { it.copy(errorMessage = null) }
                is AppResult.Failure -> {
                    _uiState.update { it.copy(errorMessage = result.message ?: "Could not add item to cart.") }
                }
            }
        }
    }
}
