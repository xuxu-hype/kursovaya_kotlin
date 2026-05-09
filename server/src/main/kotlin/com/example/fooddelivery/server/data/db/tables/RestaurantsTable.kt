package com.example.fooddelivery.server.data.db.tables

import org.jetbrains.exposed.dao.id.UUIDTable

object RestaurantsTable : UUIDTable(name = "restaurants", columnName = "id") {
    val name = text("name")
    val description = text("description").nullable()
    val imageUrl = text("image_url").nullable()
    val address = text("address").nullable()
    val rating = decimal("rating", 2, 1).default("0".toBigDecimal())
    val isOpen = bool("is_open").default(true)
}
