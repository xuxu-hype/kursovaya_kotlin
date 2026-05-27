package com.example.kursovayakotlin.presentation.orders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.domain.usecase.orders.GetOrderByIdUseCase
import com.example.kursovayakotlin.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
) : ViewModel() {
    private val orderId: String = checkNotNull(savedStateHandle[Screen.OrderDetails.ORDER_ID])

    private val _uiState = MutableStateFlow(OrderDetailsUiState(orderId = orderId, isLoading = true))
    val uiState: StateFlow<OrderDetailsUiState> = _uiState.asStateFlow()

    init {
        loadOrder()
    }

    fun loadOrder() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getOrderByIdUseCase(orderId)) {
                is AppResult.Success -> _uiState.update { it.copy(isLoading = false, order = result.data) }
                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message ?: "Could not load order.")
                    }
                }
            }
        }
    }
}
