package com.example.kursovayakotlin.presentation.profile

import androidx.lifecycle.ViewModel
import com.example.kursovayakotlin.domain.repository.AuthRepository
import com.example.kursovayakotlin.domain.usecase.auth.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState(email = authRepository.getCurrentUserEmail()))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun signOut() {
        signOutUseCase()
        _uiState.update { it.copy(email = null) }
    }
}
