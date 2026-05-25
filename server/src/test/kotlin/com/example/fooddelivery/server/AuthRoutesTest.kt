package com.example.fooddelivery.server

import com.example.fooddelivery.server.auth.FakeTokenVerifier
import com.example.fooddelivery.server.auth.FirebasePrincipal
import com.example.fooddelivery.server.auth.installFirebaseAuth
import com.example.fooddelivery.server.domain.model.MenuItem
import com.example.fooddelivery.server.domain.model.Order
import com.example.fooddelivery.server.domain.model.OrderStatus
import com.example.fooddelivery.server.domain.model.Restaurant
import com.example.fooddelivery.server.domain.model.User
import com.example.fooddelivery.server.domain.repository.NewOrderItem
import com.example.fooddelivery.server.domain.repository.OrderRepository
import com.example.fooddelivery.server.domain.repository.RestaurantRepository
import com.example.fooddelivery.server.domain.repository.UserRepository
import com.example.fooddelivery.server.plugins.ApplicationDependencies
import com.example.fooddelivery.server.plugins.configureRouting
import com.example.fooddelivery.server.plugins.configureSerialization
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import java.time.Instant
import java.util.UUID

class AuthRoutesTest {
    private val user = User(
        id = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
        firebaseUid = "firebase-user-1",
        email = "student@example.com",
        displayName = "Student User",
        phone = null,
        role = "CUSTOMER",
        createdAt = Instant.EPOCH,
    )

    @Test
    fun `protected route without token returns 401`() = testApplication {
        application {
            configureSerialization()
            installFirebaseAuth(FakeTokenVerifier(emptyMap()))
            configureRouting(dependencies = testDependencies())
        }

        val response = client.get("/me")

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `protected route with fake token succeeds`() = testApplication {
        application {
            configureSerialization()
            installFirebaseAuth(
                FakeTokenVerifier(
                    mapOf(
                        "valid-token" to FirebasePrincipal(
                            uid = user.firebaseUid,
                            email = user.email,
                            name = user.displayName,
                        ),
                    ),
                ),
            )
            configureRouting(dependencies = testDependencies())
        }

        val response = client.get("/me") {
            bearerAuth("valid-token")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains(user.firebaseUid))
        assertTrue(body.contains("student@example.com"))
    }

    private fun testDependencies(): ApplicationDependencies =
        ApplicationDependencies(
            userRepository = FixedUserRepository(user),
            restaurantRepository = EmptyRestaurantRepository(),
            orderRepository = EmptyOrderRepository(),
        )
}

private class FixedUserRepository(
    private val user: User,
) : UserRepository {
    override fun findByFirebaseUid(firebaseUid: String): User? =
        user.takeIf { it.firebaseUid == firebaseUid }

    override fun syncFirebaseUser(
        firebaseUid: String,
        email: String?,
        displayName: String?,
        phone: String?,
    ): User =
        user.copy(
            firebaseUid = firebaseUid,
            email = email,
            displayName = displayName,
            phone = phone,
        )
}

private class EmptyRestaurantRepository : RestaurantRepository {
    override fun findAll(): List<Restaurant> = emptyList()

    override fun findById(id: UUID): Restaurant? = null

    override fun findMenuByRestaurantId(restaurantId: UUID): List<MenuItem> = emptyList()

    override fun findMenuItemById(id: UUID): MenuItem? = null
}

private class EmptyOrderRepository : OrderRepository {
    override fun create(
        userId: UUID,
        restaurantId: UUID,
        status: OrderStatus,
        totalCents: Int,
        deliveryAddress: String,
        items: List<NewOrderItem>,
    ): Order {
        error("Not used in auth route tests.")
    }

    override fun findByUserId(userId: UUID): List<Order> = emptyList()

    override fun findById(id: UUID): Order? = null
}
