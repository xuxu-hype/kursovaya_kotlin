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
    private var hasSyncedAuthenticatedUser = false

    init {
        viewModelScope.launch {
            observeAuthStateUseCase().collect { isAuthenticated ->
                _uiState.update { it.copy(isAuthenticated = isAuthenticated) }
                if (isAuthenticated) {
                    syncCurrentUser()
                } else {
                    hasSyncedAuthenticatedUser = false
                }
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
                is AppResult.Success -> syncCurrentUser(force = true)
                is AppResult.Failure -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message ?: "Authentication failed.")
                    }
                }
            }
        }
    }

    suspend fun syncCurrentUser(force: Boolean = false): AppResult<Unit> {
        if (!force && hasSyncedAuthenticatedUser) {
            return AppResult.Success(Unit)
        }

        when (val result = syncMeUseCase()) {
            is AppResult.Success -> {
                hasSyncedAuthenticatedUser = true
                _uiState.update { it.copy(isLoading = false, errorMessage = null) }
                return AppResult.Success(Unit)
            }
            is AppResult.Failure -> {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message ?: "Signed in, but profile sync failed.")
                }
                return AppResult.Failure(message = result.message, cause = result.cause)
            }
        }
    }
}
