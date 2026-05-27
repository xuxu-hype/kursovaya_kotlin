package com.example.kursovayakotlin.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.domain.usecase.cart.CalculateCartTotalUseCase
import com.example.kursovayakotlin.domain.usecase.cart.ObserveCartUseCase
import com.example.kursovayakotlin.domain.usecase.cart.RemoveFromCartUseCase
import com.example.kursovayakotlin.domain.usecase.cart.UpdateCartQuantityUseCase
import com.example.kursovayakotlin.domain.usecase.orders.CreateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CartViewModel @Inject constructor(
    private val observeCartUseCase: ObserveCartUseCase,
    private val calculateCartTotalUseCase: CalculateCartTotalUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeCartUseCase().collect { items ->
                _uiState.update {
                    it.copy(items = items, totalCents = calculateCartTotalUseCase(items))
                }
            }
        }
    }

    fun onDeliveryAddressChanged(address: String) {
        _uiState.update { it.copy(deliveryAddress = address, errorMessage = null) }
    }

    fun updateQuantity(menuItemId: String, quantity: Int) {
        viewModelScope.launch {
            val result = if (quantity <= 0) {
                removeFromCartUseCase(menuItemId)
            } else {
                updateCartQuantityUseCase(menuItemId, quantity)
            }
            if (result is AppResult.Failure) {
                _uiState.update { it.copy(errorMessage = result.message ?: "Could not update cart.") }
            }
        }
    }

    fun removeItem(menuItemId: String) {
        viewModelScope.launch {
            when (val result = removeFromCartUseCase(menuItemId)) {
                is AppResult.Success -> _uiState.update { it.copy(errorMessage = null) }
                is AppResult.Failure -> {
                    _uiState.update { it.copy(errorMessage = result.message ?: "Could not remove item.") }
                }
            }
        }
    }

    fun createOrder() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, createdOrderId = null) }
            when (val result = createOrderUseCase(_uiState.value.deliveryAddress)) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            deliveryAddress = "",
                            createdOrderId = result.data.id,
                        )
                    }
                }
                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message ?: "Could not create order.")
                    }
                }
            }
        }
    }

    fun clearCreatedOrderId() {
        _uiState.update { it.copy(createdOrderId = null) }
    }
}
