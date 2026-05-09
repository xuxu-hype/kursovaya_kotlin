package com.example.fooddelivery.server.config

/**
 * Configuration from environment variables.
 * Defaults are safe for local development only (localhost Postgres, no Firebase file).
 */
data class AppConfig(
    val databaseUrl: String,
    val databaseUser: String,
    val databasePassword: String,
    /** Path to Firebase service account JSON; token verification disabled if null/blank. */
    val googleApplicationCredentials: String?,
) {
    companion object {
        fun fromEnvironment(): AppConfig =
            AppConfig(
                databaseUrl = System.getenv("DATABASE_URL")
                    ?: "jdbc:postgresql://localhost:5432/fooddelivery",
                databaseUser = System.getenv("DATABASE_USER") ?: "postgres",
                databasePassword = System.getenv("DATABASE_PASSWORD") ?: "postgres",
                googleApplicationCredentials = System.getenv("GOOGLE_APPLICATION_CREDENTIALS"),
            )
    }
}
