package com.example.fooddelivery.server.domain.usecase

sealed class UseCaseError(message: String) : RuntimeException(message) {
    class NotFound(message: String) : UseCaseError(message)
    class Validation(message: String) : UseCaseError(message)
}
