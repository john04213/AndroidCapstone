# AdventureWorks Customer Companion

Android capstone for the Enterprise Mobile Application Development Bootcamp. A customer can browse products, save them, see recently viewed products and check their orders, using the AdventureWorks API.

Team: Daniela, Harris, John, Miles.

## Features

**Products**
- Browse all products, search by name, and sort by name or price
- Product details with an availability tag
- Save and unsave products, and see them on a Saved screen
- Recently viewed products on the Discover screen, with Clear history
- List and details side by side on wide screens (tablets, landscape)

**Orders**
- The demo customer's orders, newest first, with an oldest-first option
- Search by order number
- Pull to refresh, which keeps the current list if the refresh fails
- Status shown in words, and "Not shipped yet" when there is no ship date
- Order details: order date, ship date, status, products with quantities, and the order total

**Loading and errors**
- Loading, empty and failed states with Retry, in words a customer can read (offline, timeout, server problem, bad data, not found)
- Network calls give up after 15 seconds

## Setup

1. Clone the repo and open the root folder in Android Studio. The project uses Android Gradle Plugin 9.4.1 and Kotlin 2.4.21, so use an Android Studio version that supports them.
2. Let Gradle sync.
3. Run the `app` configuration on an emulator or device with Android 8.0 (API 26) or newer. The app targets API 37.

You don't need an API key. The app needs internet access to load products and orders.

## Demo customer

This app has no sign-in. It runs as one demo customer assigned by the instructor: **Jared Peterson, customer ID 11502**. To change it, edit `core/session/DemoCustomer.kt`.

## Base URL and endpoints

The API base URL is set in one place: `BASE_URL` in `app/build.gradle.kts` (`https://api.bootcampcentral.com/api/`). Code reads it as `BuildConfig.BASE_URL`.

| Endpoint | Used for | Code |
|---|---|---|
| `GET Product` | Product list | `features/products/data/RemoteProductDataSource.kt` |
| `GET Product/{id}` | Product details | `features/products/data/RemoteProductDataSource.kt` |
| `GET Order/customer` | Order rows, filtered to the demo customer in the app | `features/orders/data/OrderApi.kt` |

## Tech stack

- Kotlin, Jetpack Compose, Material 3 (with adaptive list-detail layout)
- MVVM with `StateFlow`, and Hilt for dependency injection
- Retrofit + OkHttp + kotlinx.serialization for networking
- Room for saved and recently viewed products (`core/database/AppDatabase.kt`)
- Navigation Compose with typed routes

## Project layout

```
com.pnc.customercompanion
  core/       shared pieces: Hilt modules, demo customer, database, navigation, errors, load-state UI
  features/   one folder per feature, each split into data / domain / presentation
  ui/theme/   Material 3 theme
```

- `data`: DTOs, Retrofit services, Room entities and DAOs, mappers, repository implementations
- `domain`: models, repository interfaces, business rules. No Android, Retrofit, Room or Compose imports.
- `presentation`: screens, ViewModels, UI state

Errors travel the same way in every feature: the repository turns a library error into a `LoadException` with a `LoadFailure` kind (`core/error`), the ViewModel catches it, and the screen shows it with `FailedView` (`core/ui/StateViews.kt`).

## Running the tests

Unit tests (56):

```
./gradlew test
```

Instrumented UI tests (2, needs a running emulator):

```
./gradlew connectedAndroidTest
```

What the unit tests cover:
- Error sorting: library errors to `LoadFailure` kinds, and customer messages
- Orders: mapping API rows to orders, the repository with a fake API, sort, search, status words, ship date text
- `OrdersViewModel` and `OrderDetailViewModel`: loading, empty, success, failure, refresh, sort and search
- Orders screen helpers

The UI test checks the orders list in `androidTest/.../feature/orders/presentation/OrdersScreenUiTest.kt`.
