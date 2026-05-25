package com.example.fooddelivery.server.domain.model

import java.math.BigDecimal
import java.util.UUID

data class Restaurant(
    val id: UUID,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val address: String?,
    val rating: BigDecimal,
    val isOpen: Boolean,
)
