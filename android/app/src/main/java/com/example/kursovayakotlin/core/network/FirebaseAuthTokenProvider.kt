package com.example.kursovayakotlin.core.network

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class FirebaseAuthTokenProvider @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthTokenProvider {
    override suspend fun getToken(): String? {
        val user = firebaseAuth.currentUser ?: return null

        return runCatching {
            user.getIdToken(false).await().token
        }.getOrNull()
    }
}
