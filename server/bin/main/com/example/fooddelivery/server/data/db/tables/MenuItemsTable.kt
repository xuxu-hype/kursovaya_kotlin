package com.example.fooddelivery.server.data.db.tables

import org.jetbrains.exposed.dao.id.UUIDTable

object MenuItemsTable : UUIDTable(name = "menu_items", columnName = "id") {
    val restaurantId = reference("restaurant_id", RestaurantsTable).nullable()
    val name = text("name")
    val description = text("description").nullable()
    val priceCents = integer("price_cents")
    val imageUrl = text("image_url").nullable()
    val isAvailable = bool("is_available").default(true)
}
