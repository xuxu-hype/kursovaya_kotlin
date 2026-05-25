package com.example.kursovayakotlin.data.mapper

import com.example.kursovayakotlin.data.local.entity.MenuItemEntity
import com.example.kursovayakotlin.data.local.entity.OrderEntity
import com.example.kursovayakotlin.data.local.entity.OrderItemEntity
import com.example.kursovayakotlin.data.local.entity.RestaurantEntity
import com.example.kursovayakotlin.data.remote.dto.MenuItemDto
import com.example.kursovayakotlin.data.remote.dto.OrderDto
import com.example.kursovayakotlin.data.remote.dto.OrderItemDto
import com.example.kursovayakotlin.data.remote.dto.RestaurantDto

fun RestaurantDto.toEntity(): RestaurantEntity =
    RestaurantEntity(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        address = address,
        rating = rating,
        isOpen = isOpen,
    )

fun MenuItemDto.toEntity(): MenuItemEntity? {
    val restaurantId = restaurantId ?: return null
    return MenuItemEntity(
        id = id,
        restaurantId = restaurantId,
        name = name,
        description = description,
        priceCents = priceCents,
        imageUrl = imageUrl,
        isAvailable = isAvailable,
    )
}

fun OrderDto.toEntity(): OrderEntity? {
    val userId = userId ?: return null
    val restaurantId = restaurantId ?: return null
    return OrderEntity(
        id = id,
        userId = userId,
        restaurantId = restaurantId,
        status = status,
        totalCents = totalCents,
        deliveryAddress = deliveryAddress,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

fun OrderItemDto.toEntity(): OrderItemEntity? {
    val menuItemId = menuItemId ?: return null
    return OrderItemEntity(
        id = id,
        orderId = orderId,
        menuItemId = menuItemId,
        nameSnapshot = nameSnapshot,
        priceCents = priceCents,
        quantity = quantity,
        lineTotalCents = lineTotalCents,
    )
}
