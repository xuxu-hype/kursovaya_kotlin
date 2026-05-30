# Food Delivery Ktor Server

Kotlin JVM backend using Ktor (Netty), PostgreSQL (Neon-compatible), Flyway, Exposed, and Firebase Admin for ID token verification.

## Stack

- Kotlin/JVM and Ktor.
- Exposed for SQL access.
- Flyway for schema migrations and seed data.
- PostgreSQL, including Neon-hosted PostgreSQL.
- Firebase Admin SDK for ID token verification.
- Ktor test application and JUnit tests.

## Architecture

- `routes`: HTTP endpoints and request/response mapping.
- `domain`: models, repository interfaces, and use cases.
- `data`: Exposed table definitions, database mappers, repository implementations.
- `db`: database connection and Flyway migration startup.
- `auth`: Firebase token verifier and Ktor authentication plugin.

## Prerequisites

- JDK 21.
- Local PostgreSQL or a Neon database.
- Firebase service account JSON for protected routes.

## Environment Variables

| Variable | Purpose |
|----------|---------|
| `DATABASE_URL` | JDBC URL, e.g. `jdbc:postgresql://ep-....neon.tech/neondb?sslmode=require` |
| `DATABASE_USER` | Database user |
| `DATABASE_PASSWORD` | Database password |
| `GOOGLE_APPLICATION_CREDENTIALS` | Absolute path to Firebase service account JSON |

If `DATABASE_*` are unset, the server uses **localhost** defaults (`jdbc:postgresql://localhost:5432/fooddelivery`, user/password `postgres`) for local development only.

If `GOOGLE_APPLICATION_CREDENTIALS` is unset, Firebase token verification is disabled and protected routes reject bearer tokens until configured.

## Neon PostgreSQL

Create a Neon project, copy the JDBC host/database/user/password, then export:

```bash
export DATABASE_URL="jdbc:postgresql://<host>/<database>?sslmode=require"
export DATABASE_USER="<user>"
export DATABASE_PASSWORD="<password>"
```

Never commit Neon credentials.

## Firebase

Create a Firebase service account JSON file in Firebase Console and set:

```bash
export GOOGLE_APPLICATION_CREDENTIALS="/absolute/path/to/service-account.json"
```

Never commit service account JSON files.

## Run Locally

1. Create a database (e.g. `fooddelivery`) and set `DATABASE_URL`, `DATABASE_USER`, `DATABASE_PASSWORD` if not using defaults.
2. Export `GOOGLE_APPLICATION_CREDENTIALS` for real Firebase token verification.
3. From this directory:

```bash
./gradlew run
```

The server listens on **8080** by default (`application.conf`). Check liveness:

```bash
curl -s http://localhost:8080/health
```

Expected: `{"status":"OK"}`

## Main Endpoints

- `GET /health`
- `GET /restaurants`
- `GET /restaurants/{id}`
- `GET /restaurants/{id}/menu`
- `POST /me/sync`
- `GET /me`
- `POST /orders`
- `GET /orders/my`
- `GET /orders/{id}`

Protected endpoints require `Authorization: Bearer <firebase-id-token>`. Orders require the Firebase user to be synced first via `POST /me/sync`.

## Tests

```bash
./gradlew test
```

Unit tests use a minimal Ktor application (no database or Firebase required).

## Build

```bash
./gradlew build
```
