package com.example.fooddelivery.server

import com.example.fooddelivery.server.auth.FakeTokenVerifier
import com.example.fooddelivery.server.auth.FirebasePrincipal
import com.example.fooddelivery.server.auth.installFirebaseAuth
import com.example.fooddelivery.server.domain.model.MenuItem
import com.example.fooddelivery.server.domain.model.Order
import com.example.fooddelivery.server.domain.model.OrderItem
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
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderRoutesTest {
    private val userId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
    private val restaurantId = UUID.fromString("6b4cb4f5-bcf9-4b6d-b3f6-7fd15f5f1274")
    private val menuItemId = UUID.fromString("39c8f6d7-fcd2-4c1d-8749-5c38b3b7baf2")

    @Test
    fun `POST orders without token returns 401`() = testApplication {
        application {
            configureSerialization()
            installFirebaseAuth(fakeTokenVerifier())
            configureRouting(dependencies = testDependencies())
        }

        val response = client.post("/orders") {
            contentType(ContentType.Application.Json)
            setBody(validOrderBody())
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `POST orders with fake token and valid body returns 201`() = testApplication {
        application {
            configureSerialization()
            installFirebaseAuth(fakeTokenVerifier())
            configureRouting(dependencies = testDependencies())
        }

        val response = client.post("/orders") {
            bearerAuth("valid-token")
            contentType(ContentType.Application.Json)
            setBody(validOrderBody())
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `POST orders with non-positive quantity returns 400`() = testApplication {
        application {
            configureSerialization()
            installFirebaseAuth(fakeTokenVerifier())
            configureRouting(dependencies = testDependencies())
        }

        val response = client.post("/orders") {
            bearerAuth("valid-token")
            contentType(ContentType.Application.Json)
            setBody(validOrderBody(quantity = 0))
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `GET orders my with synced user and no orders returns 200 empty list`() = testApplication {
        application {
            configureSerialization()
            installFirebaseAuth(fakeTokenVerifier())
            configureRouting(dependencies = testDependencies())
        }

        val response = client.get("/orders/my") {
            bearerAuth("valid-token")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("[]", response.bodyAsText())
    }

    @Test
    fun `GET orders my with unsynced user returns user not synced instead of 500`() = testApplication {
        application {
            configureSerialization()
            installFirebaseAuth(fakeTokenVerifier())
            configureRouting(dependencies = testDependencies(userRepository = FakeOrderUserRepository(userId, isSynced = false)))
        }

        val response = client.get("/orders/my") {
            bearerAuth("valid-token")
        }

        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("""{"code":"USER_NOT_SYNCED","message":"Firebase user has not been synced yet.","details":null}""", response.bodyAsText())
    }

    private fun validOrderBody(quantity: Int = 2): String =
        """
        {
          "restaurantId": "$restaurantId",
          "items": [
            {
              "menuItemId": "$menuItemId",
              "quantity": $quantity
            }
          ],
          "deliveryAddress": "12 Sakura Street"
        }
        """.trimIndent()

    private fun fakeTokenVerifier(): FakeTokenVerifier =
        FakeTokenVerifier(
            mapOf(
                "valid-token" to FirebasePrincipal(
                    uid = "firebase-user-1",
                    email = "student@example.com",
                    name = "Student User",
                ),
            ),
        )

    private fun testDependencies(
        userRepository: UserRepository = FakeOrderUserRepository(userId),
    ): ApplicationDependencies =
        ApplicationDependencies(
            userRepository = userRepository,
            restaurantRepository = FakeOrderRestaurantRepository(restaurantId, menuItemId),
            orderRepository = FakeOrderRepository(),
        )
}

private class FakeOrderUserRepository(
    private val userId: UUID,
    private val isSynced: Boolean = true,
) : UserRepository {
    private val user = User(
        id = userId,
        firebaseUid = "firebase-user-1",
        email = "student@example.com",
        displayName = "Student User",
        phone = null,
        role = "CUSTOMER",
        createdAt = Instant.EPOCH,
    )

    override fun findByFirebaseUid(firebaseUid: String): User? =
        user.takeIf { isSynced && it.firebaseUid == firebaseUid }

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

private class FakeOrderRestaurantRepository(
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

private class FakeOrderRepository : OrderRepository {
    private val orderId = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb")

    override fun create(
        userId: UUID,
        restaurantId: UUID,
        status: OrderStatus,
        totalCents: Int,
        deliveryAddress: String,
        items: List<NewOrderItem>,
    ): Order =
        Order(
            id = orderId,
            userId = userId,
            restaurantId = restaurantId,
            status = status,
            totalCents = totalCents,
            deliveryAddress = deliveryAddress,
            createdAt = Instant.EPOCH,
            updatedAt = Instant.EPOCH,
            items = items.mapIndexed { index, item ->
                OrderItem(
                    id = UUID.nameUUIDFromBytes("order-item-$index".toByteArray()),
                    orderId = orderId,
                    menuItemId = item.menuItemId,
                    nameSnapshot = item.nameSnapshot,
                    priceCents = item.priceCents,
                    quantity = item.quantity,
                )
            },
        )

    override fun findByUserId(userId: UUID): List<Order> = emptyList()

    override fun findById(id: UUID): Order? = null
}
