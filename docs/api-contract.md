# Food delivery API contract

Base URL is configured per environment. All paths below are relative to that base.

- **Content type:** `application/json` for request and response bodies unless noted otherwise.
- **Money:** currency is RUB. Monetary fields are non-negative integer cents and use the `Cents` suffix, for example `priceCents`, `totalCents`, and `lineTotalCents`.
- **Timestamps:** ISO-8601 strings in UTC unless stated otherwise.

---

## Authentication

Protected routes require a Firebase ID token:

```http
Authorization: Bearer <firebase_id_token>
```

The backend verifies the bearer token and uses the Firebase UID from the verified token as the external identity. Missing or invalid tokens on protected routes return `401 Unauthorized`.

---

## Order status enum

Allowed values for order `status`:

| Value        | Meaning                |
|--------------|------------------------|
| `CREATED`    | Order placed           |
| `CONFIRMED`  | Accepted / confirmed   |
| `COOKING`    | Being prepared         |
| `ON_THE_WAY` | Out for delivery       |
| `DELIVERED`  | Completed successfully |
| `CANCELLED`  | Cancelled              |

---

## JSON DTOs

### `ErrorResponse`

```json
{
  "code": "string",
  "message": "string",
  "details": {}
}
```

| Field     | Type   | Required | Description |
|-----------|--------|----------|-------------|
| `code`    | string | yes      | Stable machine-readable code. |
| `message` | string | yes      | Human-readable summary. |
| `details` | object | no       | Optional structured hints. |

### `UserDto`

```json
{
  "id": "string",
  "firebaseUid": "string",
  "email": "string",
  "displayName": "string",
  "phone": "string",
  "role": "CUSTOMER",
  "createdAt": "string"
}
```

| Field         | Type   | Required | Description |
|---------------|--------|----------|-------------|
| `id`          | string | yes      | Internal user id. |
| `firebaseUid` | string | yes      | Firebase UID from the verified token. |
| `email`       | string | no       | Email from the token, if present. |
| `displayName` | string | no       | Display name, if present. |
| `phone`       | string | no       | Phone, if present. |
| `role`        | string | yes      | User role. |
| `createdAt`   | string | yes      | ISO-8601 timestamp. |

### `RestaurantDto`

```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "imageUrl": "string",
  "address": "string",
  "rating": 4.7,
  "isOpen": true
}
```

| Field         | Type    | Required | Description |
|---------------|---------|----------|-------------|
| `id`          | string  | yes      | Restaurant id. |
| `name`        | string  | yes      | Display name. |
| `description` | string  | no       | Short blurb. |
| `imageUrl`    | string  | no       | Cover/logo URL. |
| `address`     | string  | no       | Address text. |
| `rating`      | number  | yes      | Restaurant rating. |
| `isOpen`      | boolean | yes      | Whether the restaurant is open. |

### `MenuItemDto`

```json
{
  "id": "string",
  "restaurantId": "string",
  "name": "string",
  "description": "string",
  "priceCents": 1290,
  "imageUrl": "string",
  "isAvailable": true
}
```

| Field          | Type    | Required | Description |
|----------------|---------|----------|-------------|
| `id`           | string  | yes      | Menu item id. |
| `restaurantId` | string  | no       | Owning restaurant id. |
| `name`         | string  | yes      | Item title. |
| `description`  | string  | no       | Optional details. |
| `priceCents`   | int     | yes      | Unit price in cents. |
| `imageUrl`     | string  | no       | Optional image. |
| `isAvailable`  | boolean | yes      | If `false`, item must not be orderable. |

### `CreateOrderItemRequest`

```json
{
  "menuItemId": "string",
  "quantity": 1
}
```

| Field        | Type   | Required | Description |
|--------------|--------|----------|-------------|
| `menuItemId` | string | yes      | References `MenuItemDto.id`. |
| `quantity`   | int    | yes      | Must be greater than zero. |

### `CreateOrderRequest`

```json
{
  "restaurantId": "string",
  "items": [],
  "deliveryAddress": "string"
}
```

| Field             | Type   | Required | Description |
|-------------------|--------|----------|-------------|
| `restaurantId`    | string | yes      | Restaurant for the order. |
| `items`           | array  | yes      | Non-empty list of `CreateOrderItemRequest`. |
| `deliveryAddress` | string | yes      | Delivery address. |

Server validates that all `menuItemId` values belong to `restaurantId`, are `isAvailable`, and recomputes totals.

### `OrderItemDto`

```json
{
  "id": "string",
  "orderId": "string",
  "menuItemId": "string",
  "nameSnapshot": "string",
  "priceCents": 1290,
  "quantity": 1,
  "lineTotalCents": 1290
}
```

| Field            | Type   | Required | Description |
|------------------|--------|----------|-------------|
| `id`             | string | yes      | Order item id. |
| `orderId`        | string | yes      | Parent order id. |
| `menuItemId`     | string | no       | Original menu item id. |
| `nameSnapshot`   | string | yes      | Item name at order time. |
| `priceCents`     | int    | yes      | Unit price at order time. |
| `quantity`       | int    | yes      | Ordered quantity. |
| `lineTotalCents` | int    | yes      | `priceCents * quantity`. |

### `OrderDto`

```json
{
  "id": "string",
  "userId": "string",
  "restaurantId": "string",
  "status": "CREATED",
  "totalCents": 2580,
  "deliveryAddress": "string",
  "createdAt": "string",
  "updatedAt": "string",
  "items": []
}
```

| Field             | Type   | Required | Description |
|-------------------|--------|----------|-------------|
| `id`              | string | yes      | Order id. |
| `userId`          | string | no       | Internal user id that owns the order. |
| `restaurantId`    | string | no       | Restaurant id. |
| `status`          | string | yes      | One of the order status values. |
| `totalCents`      | int    | yes      | Server-calculated order total. |
| `deliveryAddress` | string | yes      | Stored delivery address. |
| `createdAt`       | string | yes      | ISO-8601 timestamp. |
| `updatedAt`       | string | yes      | ISO-8601 timestamp. |
| `items`           | array  | yes      | List of `OrderItemDto`. |

---

## Public endpoints

### `GET /health`

**Response `200 OK`**

```json
{
  "status": "OK"
}
```

### `GET /restaurants`

Returns a JSON array of `RestaurantDto`.

### `GET /restaurants/{id}`

Returns one `RestaurantDto`.

| Status | Code               | When |
|--------|--------------------|------|
| `400`  | `VALIDATION_ERROR` | Invalid UUID. |
| `404`  | `NOT_FOUND`        | Unknown restaurant. |

### `GET /restaurants/{id}/menu`

Returns a JSON array of `MenuItemDto`.

| Status | Code               | When |
|--------|--------------------|------|
| `400`  | `VALIDATION_ERROR` | Invalid UUID. |
| `404`  | `NOT_FOUND`        | Unknown restaurant. |

---

## Protected endpoints

### `GET /me`

Returns the persisted `UserDto` for the Firebase UID in the token.

| Status | Code           | When |
|--------|----------------|------|
| `401`  | `UNAUTHORIZED` | Missing or invalid token. |
| `404`  | `NOT_FOUND`    | User has not been synced yet. |

### `POST /me/sync`

Creates or updates the application user for the Firebase UID in the token. If the UID does not exist, the server creates a user. If it exists, the server updates `email`, `displayName`, and `phone` without changing `firebaseUid`.

```json
{
  "displayName": "string",
  "phone": "string"
}
```

| Field         | Type   | Required | Description |
|---------------|--------|----------|-------------|
| `displayName` | string | no       | Updated display name. |
| `phone`       | string | no       | Updated phone. |

Response body: `UserDto`.

### `POST /orders`

Places a new order for the authenticated user.

Request body: `CreateOrderRequest`.

Response `201 Created`: `OrderDto`.

| Status | Code               | When |
|--------|--------------------|------|
| `400`  | `VALIDATION_ERROR` | Invalid UUID, blank address, empty items, non-positive quantity, wrong restaurant, or unavailable item. |
| `401`  | `UNAUTHORIZED`     | Missing or invalid token, or synced user is missing. |
| `404`  | `NOT_FOUND`        | Restaurant or menu item not found. |

### `GET /orders/my`

Returns a JSON array of `OrderDto` for the authenticated internal user.

| Status | Code           | When |
|--------|----------------|------|
| `401`  | `UNAUTHORIZED` | Missing or invalid token, or synced user is missing. |

### `GET /orders/{id}`

Returns one `OrderDto` if it belongs to the authenticated internal user.

| Status | Code               | When |
|--------|--------------------|------|
| `400`  | `VALIDATION_ERROR` | Invalid UUID. |
| `401`  | `UNAUTHORIZED`     | Missing or invalid token, or synced user is missing. |
| `404`  | `NOT_FOUND`        | Order missing or not owned by caller. |

---

## Versioning

This document describes the initial contract. Future breaking changes may introduce a version prefix, for example `/v1`.
