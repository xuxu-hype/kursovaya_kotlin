package com.example.fooddelivery.server.data.repository

import com.example.fooddelivery.server.data.db.tables.OrderItemsTable
import com.example.fooddelivery.server.data.db.tables.OrdersTable
import com.example.fooddelivery.server.data.mapper.toOrder
import com.example.fooddelivery.server.data.mapper.toOrderItem
import com.example.fooddelivery.server.domain.model.Order
import com.example.fooddelivery.server.domain.model.OrderItem
import com.example.fooddelivery.server.domain.model.OrderStatus
import com.example.fooddelivery.server.domain.repository.NewOrderItem
import com.example.fooddelivery.server.domain.repository.OrderRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

class ExposedOrderRepository : OrderRepository {
    override fun create(
        userId: UUID,
        restaurantId: UUID,
        status: OrderStatus,
        totalCents: Int,
        deliveryAddress: String,
        items: List<NewOrderItem>,
    ): Order =
        transaction {
            val now = Instant.now()
            val orderId = UUID.randomUUID()

            OrdersTable.insert {
                it[id] = orderId
                it[OrdersTable.userId] = userId
                it[OrdersTable.restaurantId] = restaurantId
                it[OrdersTable.status] = status.name
                it[OrdersTable.totalCents] = totalCents
                it[OrdersTable.deliveryAddress] = deliveryAddress
                it[createdAt] = now
                it[updatedAt] = now
            }

            OrderItemsTable.batchInsert(items) { item ->
                this[OrderItemsTable.id] = UUID.randomUUID()
                this[OrderItemsTable.orderId] = orderId
                this[OrderItemsTable.menuItemId] = item.menuItemId
                this[OrderItemsTable.nameSnapshot] = item.nameSnapshot
                this[OrderItemsTable.priceCents] = item.priceCents
                this[OrderItemsTable.quantity] = item.quantity
            }

            findByIdInTransaction(orderId)!!
        }

    override fun findByUserId(userId: UUID): List<Order> =
        transaction {
            OrdersTable
                .selectAll()
                .where { OrdersTable.userId eq userId }
                .orderBy(OrdersTable.createdAt to SortOrder.DESC)
                .map { row ->
                    val orderId = row[OrdersTable.id].value
                    row.toOrder(loadItems(orderId))
                }
        }

    override fun findById(id: UUID): Order? =
        transaction {
            findByIdInTransaction(id)
        }

    private fun findByIdInTransaction(id: UUID): Order? {
        val row = OrdersTable
            .selectAll()
            .where { OrdersTable.id eq id }
            .singleOrNull()
            ?: return null
        return row.toOrder(loadItems(id))
    }

    private fun loadItems(orderId: UUID): List<OrderItem> =
        OrderItemsTable
            .selectAll()
            .where { OrderItemsTable.orderId eq orderId }
            .map { it.toOrderItem() }
}
