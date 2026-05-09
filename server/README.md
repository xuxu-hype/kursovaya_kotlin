# Food delivery — Ktor server

Kotlin JVM backend using Ktor (Netty), PostgreSQL (Neon-compatible), Flyway, Exposed, and Firebase Admin for ID token verification.

## Prerequisites

- JDK 21
- Local PostgreSQL **or** a Neon database (connection via environment variables only — do not commit credentials)

Optional for protected routes:

- Firebase service account JSON file on disk; path passed via `GOOGLE_APPLICATION_CREDENTIALS`

## Environment variables

| Variable | Purpose |
|----------|---------|
| `DATABASE_URL` | JDBC URL, e.g. `jdbc:postgresql://ep-....neon.tech/neondb?sslmode=require` |
| `DATABASE_USER` | Database user |
| `DATABASE_PASSWORD` | Database password |
| `GOOGLE_APPLICATION_CREDENTIALS` | Absolute path to Firebase service account JSON |

If `DATABASE_*` are unset, the server uses **localhost** defaults (`jdbc:postgresql://localhost:5432/fooddelivery`, user/password `postgres`) for local development only.

If `GOOGLE_APPLICATION_CREDENTIALS` is unset, Firebase token verification is **disabled** (bearer auth will reject all tokens until configured).

## Run locally

1. Create a database (e.g. `fooddelivery`) and set `DATABASE_URL`, `DATABASE_USER`, `DATABASE_PASSWORD` if not using defaults.
2. Export `GOOGLE_APPLICATION_CREDENTIALS` when you implement protected routes and need real token checks.
3. From this directory:

```bash
./gradlew run
```

The server listens on **8080** by default (`application.conf`). Check liveness:

```bash
curl -s http://localhost:8080/health
```

Expected: `{"status":"OK"}`

## Tests

```bash
./gradlew test
```

Unit tests use a minimal Ktor application (no database or Firebase required).

## Build

```bash
./gradlew build
```
