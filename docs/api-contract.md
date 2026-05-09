# Food delivery API contract

Base URL is configured per environment (for example `https://api.example.com`). All paths below are relative to that base.

- **Content type:** `application/json` for request and response bodies unless noted otherwise.
- **Money:** currency is **RUB** (российские рубли). All monetary fields are **non-negative integers in kopeks** (копейки; **1 ₽ = 100 коп.**). Names use the **`Kopeks`** suffix (`priceKopeks`, `deliveryFeeKopeks`, `totalKopeks`, …). No floating-point ruble amounts in JSON.
- **Timestamps:** ISO-8601 strings in UTC (e.g. `2026-05-04T12:34:56.789Z`) unless stated otherwise.

---

## Authentication

### Firebase on the client

1. The user signs in with **Firebase Authentication** on Android.
2. The app obtains a **Firebase ID token** for the signed-in user.
3. Every **protected** request must include:

```http
Authorization: Bearer <firebase_id_token>
```

The token is a JWT string from Firebase (no `Bearer` prefix inside the token value).

### Server behavior

- The Ktor backend verifies the token with the **Firebase Admin SDK**.
- On success, the backend treats **`sub` (Firebase UID)** from the token as the canonical **external user identity** for authorization and persistence (`userId` in orders, linkage for `/me`, etc.).
- Invalid, expired, or missing tokens on protected routes → **`401 Unauthorized`** with an `ErrorResponse` body (see below).

Protected routes do not use API keys in headers beyond this Bearer token.

---

## Order status enum

Allowed values for order `status` (string, uppercase):

| Value         | Meaning                                      |
|---------------|----------------------------------------------|
| `CREATED`     | Order placed, not yet confirmed by kitchen |
| `CONFIRMED`   | Accepted / confirmed                         |
| `COOKING`     | Being prepared                               |
| `ON_THE_WAY`  | Out for delivery                             |
| `DELIVERED`   | Completed successfully                       |
| `CANCELLED`   | Cancelled (terminal)                         |

---

## JSON DTOs

### `ErrorResponse`

Returned on error responses when a JSON body is present.

```json
{
  "code": "string",
  "message": "string",
  "details": {}
}
```

| Field       | Type   | Required | Description |
|------------|--------|------------|-------------|
| `code`     | string | yes        | Stable machine-readable code (e.g. `UNAUTHORIZED`, `NOT_FOUND`, `VALIDATION_ERROR`). |
| `message`  | string | yes        | Human-readable summary. |
| `details`  | object | no         | Optional structured hints (field errors, etc.). May be omitted or `null`. |

---

### `UserDto`

Represents the application user linked to Firebase.

```json
{
  "id": "string",
  "firebaseUid": "string",
  "email": "string",
  "displayName": "string",
  "phone": "string",
  "createdAt": "string",
  "updatedAt": "string"
}
```

| Field          | Type   | Required | Description |
|----------------|--------|------------|-------------|
| `id`           | string | yes        | Internal user id (server-generated UUID or similar). |
| `firebaseUid`  | string | yes        | Firebase UID; matches token `sub`. |
| `email`        | string | yes        | Email from profile / token. |
| `displayName`  | string | no         | Display name; omit or `null` if unknown. |
| `phone`        | string | no         | Phone; omit or `null` if unknown. |
| `createdAt`    | string | yes        | ISO-8601 UTC. |
| `updatedAt`    | string | yes        | ISO-8601 UTC. |

---

### `RestaurantDto`

```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "address": "string",
  "imageUrl": "string",
  "deliveryFeeKopeks": 0,
  "minOrderKopeks": 0,
  "estimatedDeliveryMinutes": 0
}
```

| Field                      | Type   | Required | Description |
|---------------------------|--------|------------|-------------|
| `id`                      | string | yes        | Restaurant id. |
| `name`                    | string | yes        | Display name. |
| `description`             | string | no         | Short blurb. |
| `address`                 | string | no         | Physical or service address text. |
| `imageUrl`                | string | no         | Cover/logo URL. |
| `deliveryFeeKopeks`     | int    | yes        | Default delivery fee, **kopeks** (RUB). |
| `minOrderKopeks`        | int    | yes        | Minimum order subtotal (excl. delivery), **kopeks** (`0` if none). |
| `estimatedDeliveryMinutes`| int    | yes        | Typical delivery time estimate. |

---

### `MenuItemDto`

```json
{
  "id": "string",
  "restaurantId": "string",
  "name": "string",
  "description": "string",
  "priceKopeks": 0,
  "imageUrl": "string",
  "available": true
}
```

| Field          | Type   | Required | Description |
|----------------|--------|------------|-------------|
| `id`           | string | yes        | Menu item id. |
| `restaurantId` | string | yes        | Owning restaurant. |
| `name`         | string | yes        | Item title. |
| `description`  | string | no         | Optional details. |
| `priceKopeks`  | int    | yes        | Unit price, **kopeks** (RUB); **≥ 0**. |
| `imageUrl`     | string | no         | Optional image. |
| `available`    | bool   | yes        | If `false`, item must not be orderable. |

---

### `CreateOrderItemRequest`

```json
{
  "menuItemId": "string",
  "quantity": 1
}
```

| Field        | Type | Required | Description |
|-------------|------|------------|-------------|
| `menuItemId`| string | yes      | References `MenuItemDto.id`. |
| `quantity`  | int    | yes      | **≥ 1**. |

---

### `CreateOrderRequest`

```json
{
  "restaurantId": "string",
  "items": [],
  "deliveryAddress": "string",
  "notes": "string"
}
```

| Field             | Type   | Required | Description |
|-------------------|--------|------------|-------------|
| `restaurantId`    | string | yes        | Restaurant for the whole order. |
| `items`           | array  | yes        | Non-empty list of `CreateOrderItemRequest`. |
| `deliveryAddress` | string | no         | Where to deliver. |
| `notes`           | string | no         | Customer notes for the restaurant. |

Server validates that all `menuItemId` belong to `restaurantId`, are `available`, and recomputes line totals (clients must not send totals as source of truth).

---

### `OrderItemDto`

Snapshot of a line on an order (prices frozen at order time).

```json
{
  "menuItemId": "string",
  "name": "string",
  "quantity": 1,
  "unitPriceKopeks": 0,
  "lineTotalKopeks": 0
}
```

| Field             | Type | Required | Description |
|-------------------|------|------------|-------------|
| `menuItemId`      | string | yes      | Original menu item id. |
| `name`            | string | yes        | Item name at order time. |
| `quantity`        | int    | yes        | **≥ 1**. |
| `unitPriceKopeks` | int    | yes        | Unit price at order time, **kopeks** (RUB). |
| `lineTotalKopeks` | int    | yes        | `quantity * unitPriceKopeks` (server-calculated), **kopeks**. |

---

### `OrderDto`

```json
{
  "id": "string",
  "userId": "string",
  "restaurantId": "string",
  "status": "CREATED",
  "items": [],
  "subtotalKopeks": 0,
  "deliveryFeeKopeks": 0,
  "totalKopeks": 0,
  "deliveryAddress": "string",
  "notes": "string",
  "createdAt": "string",
  "updatedAt": "string"
}
```

| Field              | Type   | Required | Description |
|--------------------|--------|------------|-------------|
| `id`               | string | yes        | Order id. |
| `userId`           | string | yes        | Owner’s **Firebase UID** (same as token `sub`). |
| `restaurantId`     | string | yes        | Restaurant id. |
| `status`           | string | yes        | One of **Order status enum** values. |
| `items`            | array  | yes        | List of `OrderItemDto`. |
| `subtotalKopeks`     | int    | yes        | Sum of line totals (items only), **kopeks** (RUB). |
| `deliveryFeeKopeks`  | int    | yes        | Delivery fee, **kopeks** (RUB). |
| `totalKopeks`        | int    | yes        | `subtotalKopeks + deliveryFeeKopeks` (or server’s final rule), **kopeks**. |
| `deliveryAddress`  | string | no         | Echo of request / stored value. |
| `notes`            | string | no         | Echo of request / stored value. |
| `createdAt`        | string | yes        | ISO-8601 UTC. |
| `updatedAt`        | string | yes        | ISO-8601 UTC. |

---

## Public endpoints

### `GET /health`

Liveness for load balancers and smoke tests.

**Response `200 OK`**

```json
{
  "status": "ok"
}
```

| Field    | Type   | Description        |
|----------|--------|--------------------|
| `status` | string | Fixed value `ok`. |

---

### `GET /restaurants`

List restaurants available for ordering.

**Response `200 OK`**

Body: JSON array of `RestaurantDto`.

```json
[ ]
```

---

### `GET /restaurants/{id}`

**Path parameters**

| Name | Type   | Description   |
|------|--------|---------------|
| `id` | string | Restaurant id |

**Response `200 OK`**

Body: single `RestaurantDto`.

**Errors**

| Status | `code` (typical) | When |
|--------|------------------|------|
| `404`  | `NOT_FOUND`      | Unknown `id`. |

---

### `GET /restaurants/{id}/menu`

Menu for one restaurant.

**Path parameters**

| Name | Type   | Description   |
|------|--------|---------------|
| `id` | string | Restaurant id |

**Response `200 OK`**

Body: JSON array of `MenuItemDto` for that restaurant (empty array if no items).

**Errors**

| Status | `code` (typical) | When |
|--------|------------------|------|
| `404`  | `NOT_FOUND`      | Unknown restaurant `id`. |

---

## Protected endpoints (Firebase Bearer)

All requests in this section require:

```http
Authorization: Bearer <firebase_id_token>
```

### `GET /me`

Returns the persisted user profile for the Firebase UID in the token.

**Response `200 OK`**

Body: `UserDto` for the current user.

**Errors**

| Status | `code` (typical)     | When |
|--------|----------------------|------|
| `401`  | `UNAUTHORIZED`       | Missing/invalid token. |
| `404`  | `USER_NOT_FOUND`     | No profile yet (client may call `POST /me/sync` first). |

---

### `POST /me/sync`

Creates or updates the application user record from the authenticated Firebase user (e.g. after first sign-in or profile update on device).

**Request body**

JSON object. Only mutable profile fields are required from the client; server still trusts **email and `firebaseUid` from the verified token**, not from the body.

Recommended shape (subset aligned with `UserDto` names):

```json
{
  "displayName": "string",
  "phone": "string"
}
```

| Field         | Type   | Required | Description |
|---------------|--------|------------|-------------|
| `displayName` | string | no         | Updated display name. |
| `phone`       | string | no         | Updated phone. |

Both may be omitted; server still upserts identity from token claims.

**Response `200 OK`**

Body: `UserDto` after sync.

**Errors**

| Status | `code` (typical) | When |
|--------|------------------|------|
| `401`  | `UNAUTHORIZED`   | Invalid token. |
| `400`  | `VALIDATION_ERROR` | Invalid body. |

---

### `POST /orders`

Places a new order for the authenticated user.

**Request body:** `CreateOrderRequest`

**Response `201 Created`**

Body: `OrderDto` with `status` typically `CREATED` (or first business state the server uses).

**Errors**

| Status | `code` (typical)      | When |
|--------|-----------------------|------|
| `401`  | `UNAUTHORIZED`        | Invalid token. |
| `400`  | `VALIDATION_ERROR`    | Invalid items, empty cart, wrong restaurant, unavailable items. |
| `404`  | `NOT_FOUND`           | Restaurant or menu item not found. |
| `409`  | `CONFLICT`            | Optional: business rule violation (e.g. min order not met). |

---

### `GET /orders/my`

Lists orders for the authenticated user (newest first unless documented otherwise by implementation).

**Response `200 OK`**

Body: JSON array of `OrderDto`.

```json
[ ]
```

**Errors**

| Status | `code` (typical) | When |
|--------|------------------|------|
| `401`  | `UNAUTHORIZED`   | Invalid token. |

---

### `GET /orders/{id}`

Returns one order if it belongs to the authenticated user (`OrderDto.userId` equals Firebase UID from token).

**Path parameters**

| Name | Type   | Description |
|------|--------|-------------|
| `id` | string | Order id    |

**Response `200 OK`**

Body: `OrderDto`.

**Errors**

| Status | `code` (typical) | When |
|--------|------------------|------|
| `401`  | `UNAUTHORIZED`   | Invalid token. |
| `404`  | `NOT_FOUND`      | Order missing or not owned by caller. |

---

## Optional admin / demo endpoint

### `PATCH /orders/{id}/status`

Updates order `status` for **demo or admin** flows (implementation may guard with a static admin UID, env flag, or separate admin auth—out of scope for this document).

**Path parameters**

| Name | Type   | Description |
|------|--------|-------------|
| `id` | string | Order id    |

**Request body**

```json
{
  "status": "CONFIRMED"
}
```

| Field    | Type   | Required | Description |
|----------|--------|------------|-------------|
| `status` | string | yes        | New status; must be a valid **Order status enum** value; server may restrict allowed transitions. |

**Response `200 OK`**

Body: updated `OrderDto`.

**Errors**

| Status | `code` (typical)   | When |
|--------|--------------------|------|
| `400`  | `VALIDATION_ERROR` | Invalid status or transition. |
| `401`  | `UNAUTHORIZED`     | If route is protected and token invalid. |
| `403`  | `FORBIDDEN`        | Caller not allowed (non-admin). |
| `404`  | `NOT_FOUND`        | Order not found. |

---

## Typical HTTP status summary

| Status | Usage |
|--------|--------|
| `200`  | Success with body. |
| `201`  | Created (`POST /orders`). |
| `400`  | Bad request / validation (`ErrorResponse`). |
| `401`  | Missing or invalid Bearer token (`ErrorResponse`). |
| `403`  | Authenticated but not allowed (`ErrorResponse`). |
| `404`  | Resource not found (`ErrorResponse`). |
| `409`  | Conflict / business rule (`ErrorResponse`). |
| `500`  | Server error (`ErrorResponse` optional). |

---

## Versioning

This document describes the **initial** contract. Future breaking changes may introduce a version prefix (e.g. `/v1/...`) in a later revision of this file.
