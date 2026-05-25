package com.example.fooddelivery.server.data.repository

import com.example.fooddelivery.server.data.db.tables.UsersTable
import com.example.fooddelivery.server.data.mapper.toUser
import com.example.fooddelivery.server.domain.model.User
import com.example.fooddelivery.server.domain.repository.UserRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

class ExposedUserRepository : UserRepository {
    override fun findByFirebaseUid(firebaseUid: String): User? =
        transaction {
            UsersTable
                .selectAll()
                .where { UsersTable.firebaseUid eq firebaseUid }
                .singleOrNull()
                ?.toUser()
        }

    override fun syncFirebaseUser(
        firebaseUid: String,
        email: String?,
        displayName: String?,
        phone: String?,
    ): User =
        transaction {
            UsersTable
                .selectAll()
                .where { UsersTable.firebaseUid eq firebaseUid }
                .singleOrNull()
                ?.toUser()
                ?: UsersTable.insert {
                    it[id] = UUID.randomUUID()
                    it[UsersTable.firebaseUid] = firebaseUid
                    it[UsersTable.email] = email
                    it[UsersTable.displayName] = displayName
                    it[UsersTable.phone] = phone
                    it[role] = "CUSTOMER"
                    it[createdAt] = Instant.now()
                }.resultedValues!!.single().toUser()
        }
}
