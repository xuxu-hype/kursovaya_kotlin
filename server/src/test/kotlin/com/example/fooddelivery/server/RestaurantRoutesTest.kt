package com.example.fooddelivery.server

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
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class RestaurantRoutesTest {
    private val restaurantId = UUID.fromString("6b4cb4f5-bcf9-4b6d-b3f6-7fd15f5f1274")
    private val menuItemId = UUID.fromString("39c8f6d7-fcd2-4c1d-8749-5c38b3b7baf2")

    @Test
    fun `GET restaurants returns public restaurant list`() = testApplication {
        application {
            configureSerialization()
            configureRouting(dependencies = testDependencies(), installProtectedRoutes = false)
        }

        val response = client.get("/restaurants")

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("Tokyo Bento"))
        assertTrue(body.contains(restaurantId.toString()))
    }

    @Test
    fun `GET restaurant menu returns public menu list`() = testApplication {
        application {
            configureSerialization()
            configureRouting(dependencies = testDependencies(), installProtectedRoutes = false)
        }

        val response = client.get("/restaurants/$restaurantId/menu")

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("Salmon Nigiri Set"))
        assertTrue(body.contains("\"priceCents\":1290"))
    }

    @Test
    fun `GET missing restaurant returns 404`() = testApplication {
        application {
            configureSerialization()
            configureRouting(dependencies = testDependencies(), installProtectedRoutes = false)
        }

        val response = client.get("/restaurants/11111111-1111-1111-1111-111111111111")

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    private fun testDependencies(): ApplicationDependencies =
        ApplicationDependencies(
            userRepository = NoopUserRepository(),
            restaurantRepository = FakeRestaurantRepository(restaurantId, menuItemId),
            orderRepository = NoopOrderRepository(),
        )
}

private class FakeRestaurantRepository(
    private val restaurantId: UUID,
    private val menuItemId: UUID,
) : RestaurantRepository {
    private val restaurant = Restaurant(
        id = restaurantId,
        name = "Tokyo Bento",
        description = "Japanese comfort food.",
        imageUrl = null,
        address = "12 Sakura Street",
        rating = BigDecimal("4.7"),
        isOpen = true,
    )

    private val menuItem = MenuItem(
        id = menuItemId,
        restaurantId = restaurantId,
        name = "Salmon Nigiri Set",
        description = "Six pieces of salmon nigiri.",
        priceCents = 1290,
        imageUrl = null,
        isAvailable = true,
    )

    override fun findAll(): List<Restaurant> = listOf(restaurant)

    override fun findById(id: UUID): Restaurant? = restaurant.takeIf { it.id == id }

    override fun findMenuByRestaurantId(restaurantId: UUID): List<MenuItem> =
        if (restaurantId == this.restaurantId) listOf(menuItem) else emptyList()

    override fun findMenuItemById(id: UUID): MenuItem? = menuItem.takeIf { it.id == id }
}

private class NoopUserRepository : UserRepository {
    override fun findByFirebaseUid(firebaseUid: String): User? = null

    override fun syncFirebaseUser(
        firebaseUid: String,
        email: String?,
        displayName: String?,
        phone: String?,
    ): User =
        User(
            id = UUID.randomUUID(),
            firebaseUid = firebaseUid,
            email = email,
            displayName = displayName,
            phone = phone,
            role = "CUSTOMER",
            createdAt = Instant.EPOCH,
        )
}

private class NoopOrderRepository : OrderRepository {
    override fun create(
        userId: UUID,
        restaurantId: UUID,
        status: OrderStatus,
        totalCents: Int,
        deliveryAddress: String,
        items: List<NewOrderItem>,
    ): Order {
        error("Not used in restaurant route tests.")
    }

    override fun findByUserId(userId: UUID): List<Order> = emptyList()

    override fun findById(id: UUID): Order? = null
}
