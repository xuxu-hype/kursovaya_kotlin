# Мобильное приложение для заказа и доставки еды

## Описание проекта

Данный проект представляет собой клиент-серверное мобильное приложение для заказа и доставки еды. Приложение позволяет пользователю зарегистрироваться, войти в систему, просматривать список ресторанов, открывать меню ресторана, добавлять блюда в корзину, оформлять заказ и просматривать историю заказов.

Проект разработан в рамках курсовой работы по разработке клиент-серверных мобильных приложений.

## Основные возможности

- регистрация и вход пользователя через Firebase Authentication;
- синхронизация профиля пользователя с сервером;
- просмотр списка ресторанов;
- просмотр меню выбранного ресторана;
- отображение фотографий блюд;
- добавление блюд в корзину;
- изменение количества блюд в корзине;
- оформление заказа с указанием адреса доставки;
- просмотр истории заказов;
- просмотр деталей заказа;
- выход из аккаунта;
- локальное хранение корзины через Room;
- взаимодействие клиента и сервера через REST API.

## Используемые технологии

### Клиентская часть

- Kotlin;
- Android;
- Jetpack Compose;
- Material Design 3;
- Navigation Compose;
- ViewModel;
- StateFlow;
- Retrofit;
- OkHttp;
- Room;
- Hilt;
- Firebase Authentication;
- Coil.

### Серверная часть

- Kotlin;
- Ktor;
- PostgreSQL;
- Neon PostgreSQL;
- Exposed;
- HikariCP;
- Flyway;
- Firebase Admin SDK;
- REST API.

## Архитектура проекта

Проект разделен на две основные части:

```text
android/ — мобильное Android-приложение
server/  — серверная часть на Ktor
docs/    — документация проекта
```

Клиентская часть построена по принципам Clean Architecture и разделена на слои:

```text
presentation — экраны, ViewModel, состояние интерфейса
domain       — модели, интерфейсы репозиториев, use case-классы
data         — Retrofit API, Room, DTO, Entity, реализации репозиториев
core         — общие классы результата и сетевой логики
di           — Hilt-модули для внедрения зависимостей
```

Серверная часть также разделена на логические слои:

```text
routes       — HTTP-маршруты Ktor
domain       — доменные модели, репозитории, use case-классы
data         — реализация репозиториев и работа с Exposed
db           — подключение к PostgreSQL
auth         — проверка Firebase ID token
config       — конфигурация приложения
plugins      — настройка Ktor-плагинов
```

## Настройка Firebase

Для работы авторизации необходимо создать проект в Firebase Console.

1. Открыть Firebase Console.
2. Создать новый проект.
3. Добавить Android-приложение с package name:

```text
com.example.kursovayakotlin
```

4. Скачать файл `google-services.json`.
5. Поместить файл в папку:

```text
android/app/google-services.json
```

6. Включить способ входа:

```text
Authentication → Sign-in method → Email/Password
```

7. Для серверной части скачать Firebase service account:

```text
Project settings → Service accounts → Generate new private key
```

Файл service account не должен храниться в репозитории.

## Настройка PostgreSQL Neon

Для работы серверной части необходима удаленная база данных PostgreSQL в Neon.

1. Зарегистрироваться на сайте Neon.
2. Создать новый проект PostgreSQL.
3. Скопировать строку подключения.
4. Перед запуском сервера задать переменные окружения:

```bash
export DATABASE_URL='jdbc:postgresql://HOST/neondb?sslmode=require'
export DATABASE_USER='neondb_owner'
export DATABASE_PASSWORD='PASSWORD'
export GOOGLE_APPLICATION_CREDENTIALS='/path/to/firebase-service-account.json'
```

Пароли и ключи не должны сохраняться в исходном коде.

## Запуск серверной части

Перейти в папку сервера:

```bash
cd server
```

Запустить сервер:

```bash
./gradlew run
```

Проверить работу сервера:

```bash
curl http://localhost:8080/health
```

Ожидаемый ответ:

```json
{ "status": "OK" }
```

Проверить список ресторанов:

```bash
curl http://localhost:8080/restaurants
```

## Запуск Android-приложения

1. Открыть папку `android/` в Android Studio.
2. Дождаться Gradle Sync.
3. Убедиться, что файл `google-services.json` находится в папке `android/app/`.
4. Запустить приложение на эмуляторе или реальном устройстве.

При запуске на Android Emulator базовый адрес сервера:

```text
http://10.0.2.2:8080/
```

При запуске на реальном телефоне нужно использовать IP-адрес компьютера в локальной сети, например:

```text
http://192.168.1.12:8080/
```

IP-адрес указывается в `BuildConfig.BASE_URL`.

## Основные API endpoint-ы

### Публичные endpoint-ы

```text
GET /health
GET /restaurants
GET /restaurants/{id}
GET /restaurants/{id}/menu
```

### Защищенные endpoint-ы

```text
POST /me/sync
GET /me
POST /orders
GET /orders/my
GET /orders/{id}
```

Защищенные endpoint-ы требуют заголовок:

```text
Authorization: Bearer <Firebase ID token>
```

## Структура базы данных

В серверной базе данных используются таблицы:

```text
users        — пользователи приложения
restaurants  — рестораны
menu_items   — блюда ресторанов
orders       — заказы пользователей
order_items  — состав заказов
```

Миграции базы данных находятся в папке:

```text
server/src/main/resources/db/migration/
```

## Запуск тестов

### Тесты серверной части

```bash
cd server
./gradlew test
```

Отчет о тестах:

```text
server/build/reports/tests/test/index.html
```

### Тесты Android-приложения

```bash
cd android
./gradlew test
```

Или:

```bash
./gradlew testDebugUnitTest
```

Отчет о тестах:

```text
android/app/build/reports/tests/testDebugUnitTest/index.html
```

## Демонстрационный сценарий

Для демонстрации работы приложения можно использовать следующий сценарий:

1. Запустить Ktor-сервер.
2. Запустить Android-приложение.
3. Зарегистрировать нового пользователя.
4. Выполнить вход в приложение.
5. Открыть список ресторанов.
6. Выбрать ресторан.
7. Открыть меню ресторана.
8. Добавить блюдо в корзину.
9. Перейти во вкладку Cart.
10. Ввести адрес доставки.
11. Создать заказ.
12. Открыть вкладку Orders.
13. Проверить, что заказ отображается в истории.
14. Открыть детали заказа.
15. Выйти из аккаунта через Profile.

## Возможные улучшения

В дальнейшем приложение можно расширить следующими функциями:

- административная панель для управления ресторанами и меню;
- изменение статусов заказа администратором;
- push-уведомления о статусе заказа;
- онлайн-оплата;
- карта доставки;
- отзывы и рейтинги ресторанов;
- фильтрация ресторанов и блюд;
- избранные блюда;
- более развитый офлайн-режим;
- поддержка ролей администратора и курьера.

## Безопасность

В репозиторий не должны попадать:

```text
google-services.json
firebase-service-account.json
.env
local.properties
DATABASE_PASSWORD
DATABASE_URL с паролем
```

Эти файлы и значения должны храниться только локально или в переменных окружения.
