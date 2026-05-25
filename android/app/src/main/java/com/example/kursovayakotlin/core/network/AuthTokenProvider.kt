package com.example.kursovayakotlin.core.network

interface AuthTokenProvider {
    fun getToken(): String?
}
