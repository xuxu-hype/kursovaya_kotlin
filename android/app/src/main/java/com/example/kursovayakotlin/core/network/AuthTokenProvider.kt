package com.example.kursovayakotlin.core.network

interface AuthTokenProvider {
    suspend fun getToken(): String?
}
