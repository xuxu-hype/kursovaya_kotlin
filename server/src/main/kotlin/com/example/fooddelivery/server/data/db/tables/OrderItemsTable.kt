package com.example.fooddelivery.server.data.db.tables

import org.jetbrains.exposed.dao.id.UUIDTable

object OrderItemsTable : UUIDTable(name = "order_items", columnName = "id") {
    val orderId = reference("order_id", OrdersTable).nullable()
    val menuItemId = reference("menu_item_id", MenuItemsTable).nullable()
    val nameSnapshot = text("name_snapshot")
    val priceCents = integer("price_cents")
    val quantity = integer("quantity")
}
