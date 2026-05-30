package com.example.kursovayakotlin.presentation.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.domain.usecase.auth.SyncMeUseCase
import com.example.kursovayakotlin.domain.usecase.orders.ObserveOrdersUseCase
import com.example.kursovayakotlin.domain.usecase.orders.RefreshMyOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val observeOrdersUseCase: ObserveOrdersUseCase,
    private val refreshMyOrdersUseCase: RefreshMyOrdersUseCase,
    private val syncMeUseCase: SyncMeUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrdersUiState(isLoading = true))
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeOrdersUseCase().collect { orders ->
                _uiState.update { it.copy(orders = orders) }
            }
        }
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val syncResult = syncMeUseCase()) {
                is AppResult.Success -> Unit
                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = syncResult.message ?: "Could not sync profile.")
                    }
                    return@launch
                }
            }
            refreshMyOrders(syncOnUserNotSynced = true)
        }
    }

    private suspend fun refreshMyOrders(syncOnUserNotSynced: Boolean) {
        when (val result = refreshMyOrdersUseCase()) {
            is AppResult.Success -> _uiState.update { it.copy(isLoading = false) }
            is AppResult.Failure -> {
                if (syncOnUserNotSynced && result.code == "USER_NOT_SYNCED") {
                    when (val syncResult = syncMeUseCase()) {
                        is AppResult.Success -> refreshMyOrders(syncOnUserNotSynced = false)
                        is AppResult.Failure -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = syncResult.message ?: "Could not sync profile.",
                                )
                            }
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message ?: "Could not load orders.")
                    }
                }
            }
        }
    }
}
