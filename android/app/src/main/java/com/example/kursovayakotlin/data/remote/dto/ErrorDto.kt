package com.example.kursovayakotlin.data.remote.dto

data class ErrorDto(
    val code: String,
    val message: String,
    val details: Map<String, String>? = null,
)
