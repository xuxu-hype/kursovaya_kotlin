# Android Client

Kotlin Android client for the food delivery MVP.

## Stack

- Kotlin and Jetpack Compose for UI.
- Hilt for dependency injection.
- Retrofit and OkHttp for API calls.
- Firebase Auth for email/password sign-in and ID tokens.
- Room/SQLite for local restaurants, menu, cart, and order cache.
- Clean Architecture-style package split: `presentation`, `domain`, `data`, `core`, `di`.

## Firebase Setup

1. Enable Email/Password auth in Firebase Console.
2. Download the Android `google-services.json`.
3. Place it at:

```text
android/app/google-services.json
```

The file is ignored by git and must stay local. Without it, the Gradle project can still be inspected, but real Firebase auth will not work.

## Backend URL

Set `BASE_URL` in `android/app/build.gradle.kts` to a server URL reachable by the device:

- Android emulator: `http://10.0.2.2:8080/`
- Real device: `http://<your-computer-lan-ip>:8080/`

Keep the trailing slash.

## Main Flow

1. `AuthViewModel` signs in/signs up with Firebase.
2. The app calls `POST /me/sync` to create/update the backend user.
3. Restaurants and menu are loaded from the Ktor backend.
4. Cart data is stored locally with Room.
5. `CartViewModel` syncs the user again before `POST /orders`.
6. Created order is saved locally and the cart is cleared.
7. Orders screen syncs the user before `GET /orders/my` and shows backend errors instead of silently showing an empty list.

## Run

Open `android/` in Android Studio and run the `app` configuration, or use:

```bash
./gradlew installDebug
```

## Tests And Build

```bash
./gradlew test
./gradlew build
```

Current unit coverage includes cart total calculation, Room DAOs, and repository behavior for cart/order flows.
