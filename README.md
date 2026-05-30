# University Food Delivery MVP

Client-server food delivery app for a university coursework defense. The MVP supports Firebase sign-in/sign-up, restaurant browsing, menu loading, cart management, order creation, order history/details, and a simple profile with sign out.

## Tech Stack

- Android: Kotlin, Jetpack Compose, Hilt, Retrofit, Room/SQLite, Firebase Auth.
- Backend: Kotlin, Ktor, Exposed, Flyway, PostgreSQL.
- Database: Neon PostgreSQL or local PostgreSQL.
- Auth: Firebase ID tokens from Android, verified by Firebase Admin on the backend.
- Tests: JUnit, Room test utilities, Ktor test application.

## Architecture

- `android/` is the mobile client.
- `server/` is the Ktor API.
- Android follows Clean Architecture-style layers:
  - `presentation`: Compose screens and ViewModels.
  - `domain`: models, repository interfaces, use cases.
  - `data`: Retrofit API, Room DAOs/entities, repository implementations, mappers.
- Backend separates routes, domain use cases, repositories, database tables, and DTOs.

## Configure Neon PostgreSQL

1. Create a Neon project and database.
2. Copy the JDBC connection details.
3. Export environment variables before running the server:

```bash
export DATABASE_URL="jdbc:postgresql://<host>/<database>?sslmode=require"
export DATABASE_USER="<user>"
export DATABASE_PASSWORD="<password>"
```

Do not commit database passwords. If these variables are unset, the server uses local PostgreSQL defaults for development.

## Configure Firebase

1. Create a Firebase project.
2. Enable Email/Password authentication.
3. Android: download `google-services.json` and place it at `android/app/google-services.json`.
4. Backend: create a Firebase service account JSON file and set:

```bash
export GOOGLE_APPLICATION_CREDENTIALS="/absolute/path/to/service-account.json"
```

Do not commit `google-services.json` or service account JSON files. They are ignored by git.

## Run Backend

```bash
cd server
./gradlew run
```

The API listens on `http://localhost:8080` by default. Check it with:

```bash
curl http://localhost:8080/health
```

## Run Android

1. Put `android/app/google-services.json` in place.
2. Ensure `BASE_URL` in `android/app/build.gradle.kts` points to the backend address reachable from the emulator/device, for example `http://10.0.2.2:8080/` for the Android emulator or your LAN IP for a real phone.
3. Open `android/` in Android Studio and run the `app` configuration.

## Run Tests

```bash
cd server
./gradlew test

cd ../android
./gradlew test
./gradlew build
```

## Demo Scenario

1. Sign up or sign in with Firebase email/password.
2. The app syncs the Firebase user with `POST /me/sync`.
3. Open Restaurants.
4. Open a restaurant menu.
5. Add a menu item to Cart.
6. Change quantity.
7. Enter delivery address.
8. Create order.
9. Backend returns `201 Created` for `POST /orders`.
10. Cart is cleared.
11. Orders tab shows the created order with status and total.
12. Open order details and show the item list.
13. Open Profile, show email, and sign out.
