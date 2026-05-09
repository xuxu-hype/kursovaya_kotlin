package com.example.fooddelivery.server.data.db.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.timestamp

object OrdersTable : UUIDTable(name = "orders", columnName = "id") {
    val userId = reference("user_id", UsersTable).nullable()
    val restaurantId = reference("restaurant_id", RestaurantsTable).nullable()
    val status = text("status")
    val totalCents = integer("total_cents")
    val deliveryAddress = text("delivery_address")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}
