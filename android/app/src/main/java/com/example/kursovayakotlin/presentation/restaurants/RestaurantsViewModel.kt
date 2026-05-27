package com.example.kursovayakotlin.presentation.restaurants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.domain.usecase.restaurants.ObserveRestaurantsUseCase
import com.example.kursovayakotlin.domain.usecase.restaurants.RefreshRestaurantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val observeRestaurantsUseCase: ObserveRestaurantsUseCase,
    private val refreshRestaurantsUseCase: RefreshRestaurantsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RestaurantsUiState(isLoading = true))
    val uiState: StateFlow<RestaurantsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeRestaurantsUseCase().collect { restaurants ->
                _uiState.update { it.copy(restaurants = restaurants) }
            }
        }
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = refreshRestaurantsUseCase()) {
                is AppResult.Success -> _uiState.update { it.copy(isLoading = false) }
                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message ?: "Could not load restaurants.")
                    }
                }
            }
        }
    }
}
