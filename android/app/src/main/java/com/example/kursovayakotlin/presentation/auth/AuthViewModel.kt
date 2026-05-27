package com.example.kursovayakotlin.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.domain.usecase.auth.ObserveAuthStateUseCase
import com.example.kursovayakotlin.domain.usecase.auth.SignInUseCase
import com.example.kursovayakotlin.domain.usecase.auth.SignUpUseCase
import com.example.kursovayakotlin.domain.usecase.auth.SyncMeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val syncMeUseCase: SyncMeUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeAuthStateUseCase().collect { isAuthenticated ->
                _uiState.update { it.copy(isAuthenticated = isAuthenticated) }
            }
        }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun signIn() {
        submit { email, password -> signInUseCase(email, password) }
    }

    fun signUp() {
        submit { email, password -> signUpUseCase(email, password) }
    }

    private fun submit(authAction: suspend (String, String) -> AppResult<Unit>) {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password

        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Enter email and password.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = authAction(email, password)) {
                is AppResult.Success -> syncCurrentUser()
                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message ?: "Authentication failed.")
                    }
                }
            }
        }
    }

    private suspend fun syncCurrentUser() {
        when (val result = syncMeUseCase()) {
            is AppResult.Success -> _uiState.update { it.copy(isLoading = false, errorMessage = null) }
            is AppResult.Failure -> {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message ?: "Signed in, but profile sync failed.")
                }
            }
        }
    }
}
