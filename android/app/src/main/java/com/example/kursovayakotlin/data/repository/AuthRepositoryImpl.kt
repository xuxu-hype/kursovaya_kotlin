package com.example.kursovayakotlin.data.repository

import com.example.kursovayakotlin.core.result.AppResult
import com.example.kursovayakotlin.data.mapper.toDomain
import com.example.kursovayakotlin.data.remote.api.FoodApi
import com.example.kursovayakotlin.data.remote.dto.SyncUserRequestDto
import com.example.kursovayakotlin.domain.model.User
import com.example.kursovayakotlin.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val foodApi: FoodApi,
) : AuthRepository {
    override fun observeAuthState(): Flow<Boolean> =
        callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                trySend(auth.currentUser != null)
            }
            firebaseAuth.addAuthStateListener(listener)
            trySend(firebaseAuth.currentUser != null)
            awaitClose { firebaseAuth.removeAuthStateListener(listener) }
        }

    override suspend fun signIn(email: String, password: String): AppResult<Unit> =
        runCatching {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        }.fold(
            onSuccess = { AppResult.Success(Unit) },
            onFailure = { AppResult.Failure(message = it.message, cause = it) },
        )

    override suspend fun signUp(email: String, password: String): AppResult<Unit> =
        runCatching {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        }.fold(
            onSuccess = { AppResult.Success(Unit) },
            onFailure = { AppResult.Failure(message = it.message, cause = it) },
        )

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun syncMe(): AppResult<User> =
        runCatching {
            foodApi.syncMe(
                SyncUserRequestDto(
                    displayName = firebaseAuth.currentUser?.displayName,
                    phone = null,
                ),
            ).toDomain()
        }.fold(
            onSuccess = { AppResult.Success(it) },
            onFailure = { AppResult.Failure(message = it.message, cause = it) },
        )

    override fun getCurrentUserEmail(): String? =
        firebaseAuth.currentUser?.email
}
