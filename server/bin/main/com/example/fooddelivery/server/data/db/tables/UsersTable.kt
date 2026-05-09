package com.example.fooddelivery.server.data.db.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.timestamp

object UsersTable : UUIDTable(name = "users", columnName = "id") {
    val firebaseUid = text("firebase_uid").uniqueIndex()
    val email = text("email").nullable()
    val displayName = text("display_name").nullable()
    val phone = text("phone").nullable()
    val role = text("role").default("CUSTOMER")
    val createdAt = timestamp("created_at")
}
