package com.example.fooddelivery.server.data.mapper

import com.example.fooddelivery.server.data.db.tables.MenuItemsTable
import com.example.fooddelivery.server.data.db.tables.OrderItemsTable
import com.example.fooddelivery.server.data.db.tables.OrdersTable
import com.example.fooddelivery.server.data.db.tables.RestaurantsTable
import com.example.fooddelivery.server.data.db.tables.UsersTable
import com.example.fooddelivery.server.domain.model.MenuItem
import com.example.fooddelivery.server.domain.model.Order
import com.example.fooddelivery.server.domain.model.OrderItem
import com.example.fooddelivery.server.domain.model.OrderStatus
import com.example.fooddelivery.server.domain.model.Restaurant
import com.example.fooddelivery.server.domain.model.User
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toUser(): User =
    User(
        id = this[UsersTable.id].value,
        firebaseUid = this[UsersTable.firebaseUid],
        email = this[UsersTable.email],
        displayName = this[UsersTable.displayName],
        phone = this[UsersTable.phone],
        role = this[UsersTable.role],
        createdAt = this[UsersTable.createdAt],
    )

fun ResultRow.toRestaurant(): Restaurant =
    Restaurant(
        id = this[RestaurantsTable.id].value,
        name = this[RestaurantsTable.name],
        description = this[RestaurantsTable.description],
        imageUrl = this[RestaurantsTable.imageUrl],
        address = this[RestaurantsTable.address],
        rating = this[RestaurantsTable.rating],
        isOpen = this[RestaurantsTable.isOpen],
    )

fun ResultRow.toMenuItem(): MenuItem =
    MenuItem(
        id = this[MenuItemsTable.id].value,
        restaurantId = this[MenuItemsTable.restaurantId]?.value,
        name = this[MenuItemsTable.name],
        description = this[MenuItemsTable.description],
        priceCents = this[MenuItemsTable.priceCents],
        imageUrl = this[MenuItemsTable.imageUrl],
        isAvailable = this[MenuItemsTable.isAvailable],
    )

fun ResultRow.toOrder(items: List<OrderItem>): Order =
    Order(
        id = this[OrdersTable.id].value,
        userId = this[OrdersTable.userId]?.value,
        restaurantId = this[OrdersTable.restaurantId]?.value,
        status = OrderStatus.valueOf(this[OrdersTable.status]),
        totalCents = this[OrdersTable.totalCents],
        deliveryAddress = this[OrdersTable.deliveryAddress],
        createdAt = this[OrdersTable.createdAt],
        updatedAt = this[OrdersTable.updatedAt],
        items = items,
    )

fun ResultRow.toOrderItem(): OrderItem =
    OrderItem(
        id = this[OrderItemsTable.id].value,
        orderId = this[OrderItemsTable.orderId]!!.value,
        menuItemId = this[OrderItemsTable.menuItemId]?.value,
        nameSnapshot = this[OrderItemsTable.nameSnapshot],
        priceCents = this[OrderItemsTable.priceCents],
        quantity = this[OrderItemsTable.quantity],
    )
