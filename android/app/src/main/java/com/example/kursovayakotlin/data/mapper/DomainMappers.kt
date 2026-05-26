package com.example.kursovayakotlin.data.mapper

import com.example.kursovayakotlin.data.local.entity.CartItemEntity
import com.example.kursovayakotlin.data.local.entity.MenuItemEntity
import com.example.kursovayakotlin.data.local.entity.OrderEntity
import com.example.kursovayakotlin.data.local.entity.OrderItemEntity
import com.example.kursovayakotlin.data.local.entity.RestaurantEntity
import com.example.kursovayakotlin.domain.model.CartItem
import com.example.kursovayakotlin.domain.model.MenuItem
import com.example.kursovayakotlin.domain.model.Order
import com.example.kursovayakotlin.domain.model.OrderItem
import com.example.kursovayakotlin.domain.model.OrderStatus
import com.example.kursovayakotlin.domain.model.Restaurant

fun RestaurantEntity.toDomain(): Restaurant =
    Restaurant(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        address = address,
        rating = rating,
        isOpen = isOpen,
    )

fun MenuItemEntity.toDomain(): MenuItem =
    MenuItem(
        id = id,
        restaurantId = restaurantId,
        name = name,
        description = description,
        priceCents = priceCents,
        imageUrl = imageUrl,
        isAvailable = isAvailable,
    )

fun CartItemEntity.toDomain(): CartItem =
    CartItem(
        menuItemId = menuItemId,
        restaurantId = restaurantId,
        name = name,
        priceCents = priceCents,
        imageUrl = imageUrl,
        quantity = quantity,
    )

fun MenuItem.toCartItemEntity(quantity: Int = 1): CartItemEntity =
    CartItemEntity(
        menuItemId = id,
        restaurantId = restaurantId,
        name = name,
        priceCents = priceCents,
        imageUrl = imageUrl,
        quantity = quantity,
    )

fun OrderItemEntity.toDomain(): OrderItem =
    OrderItem(
        id = id,
        orderId = orderId,
        menuItemId = menuItemId,
        nameSnapshot = nameSnapshot,
        priceCents = priceCents,
        quantity = quantity,
        lineTotalCents = lineTotalCents,
    )

fun OrderEntity.toDomain(items: List<OrderItemEntity>): Order =
    Order(
        id = id,
        userId = userId,
        restaurantId = restaurantId,
        status = runCatching { OrderStatus.valueOf(status) }.getOrDefault(OrderStatus.CREATED),
        totalCents = totalCents,
        deliveryAddress = deliveryAddress,
        createdAt = createdAt,
        updatedAt = updatedAt,
        items = items.map { it.toDomain() },
    )
