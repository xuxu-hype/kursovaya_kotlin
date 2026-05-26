package com.example.kursovayakotlin.data.mapper

import com.example.kursovayakotlin.data.remote.dto.UserDto
import com.example.kursovayakotlin.domain.model.User

fun UserDto.toDomain(): User =
    User(
        id = id,
        firebaseUid = firebaseUid,
        email = email,
        displayName = displayName,
        phone = phone,
        role = role,
        createdAt = createdAt,
    )
