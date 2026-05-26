package com.example.kursovayakotlin.domain.repository

import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeAuthState(): Flow<Boolean>

    suspend fun signIn(email: String, password: String): AppResult<Unit>

    suspend fun signUp(email: String, password: String): AppResult<Unit>

    fun signOut()

    suspend fun syncMe(): AppResult<User>

    fun getCurrentUserEmail(): String?
}
