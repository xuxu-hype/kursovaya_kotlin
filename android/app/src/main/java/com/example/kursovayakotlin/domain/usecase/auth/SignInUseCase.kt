package com.example.kursovayakotlin.domain.usecase.auth

import com.example.kursovayakotlin.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.signIn(email, password)
}
