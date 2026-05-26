package com.example.kursovayakotlin.domain.usecase.auth

import com.example.kursovayakotlin.domain.repository.AuthRepository
import javax.inject.Inject

class ObserveAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke() = authRepository.observeAuthState()
}
