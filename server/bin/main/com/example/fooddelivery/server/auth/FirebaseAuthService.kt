package com.example.fooddelivery.server.auth

import com.example.fooddelivery.server.config.AppConfig
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.slf4j.LoggerFactory
import java.io.FileInputStream

class FirebaseAuthService private constructor(
    private val firebaseAuth: FirebaseAuth?,
) {
    private val logger = LoggerFactory.getLogger(FirebaseAuthService::class.java)

    fun verifyIdToken(idToken: String): FirebasePrincipal? {
        val auth = firebaseAuth ?: return null
        return try {
            val decoded = auth.verifyIdToken(idToken)
            FirebasePrincipal(uid = decoded.uid, email = decoded.email)
        } catch (e: FirebaseAuthException) {
            logger.debug("Firebase token rejected: {}", e.message)
            null
        } catch (e: Exception) {
            logger.warn("Unexpected error verifying Firebase token", e)
            null
        }
    }

    companion object {
        fun create(config: AppConfig): FirebaseAuthService {
            val path = config.googleApplicationCredentials?.trim().orEmpty()
            if (path.isEmpty()) {
                LoggerFactory.getLogger(FirebaseAuthService::class.java)
                    .info("GOOGLE_APPLICATION_CREDENTIALS not set; Firebase token verification is disabled.")
                return FirebaseAuthService(firebaseAuth = null)
            }
            if (FirebaseApp.getApps().isEmpty()) {
                FileInputStream(path).use { stream ->
                    val options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(stream))
                        .build()
                    FirebaseApp.initializeApp(options)
                }
            }
            return FirebaseAuthService(FirebaseAuth.getInstance())
        }
    }
}
