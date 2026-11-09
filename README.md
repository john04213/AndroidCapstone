# AdventureWorks Customer Companion

Android capstone for the Enterprise Mobile Application Development Bootcamp. A customer can browse products, save them, see recently viewed products and check their orders, using the AdventureWorks API.

Team: Daniela, Harris, John, Miles.

## Setup

TODO: Android Studio version, how to open the project, how to run it on an emulator.

## Demo customer

This app has no sign-in. It runs as one demo customer assigned by the instructor.

TODO: say which customer it is and that it is set in `core/session/DemoCustomer.kt`.

## Base URL

The API base URL is set in one place: `BASE_URL` in `app/build.gradle.kts`. Code reads it as `BuildConfig.BASE_URL`.

TODO: link to the Swagger page and list the endpoints the app uses.

## Project layout

```
com.pnc.customercompanion
  core/       shared pieces: Hilt modules, demo customer, database, navigation, load-state UI
  features/   one folder per feature, each split into data / domain / presentation
  ui/theme/   Material 3 theme
```

- `data`: DTOs, Retrofit services, Room entities and DAOs, mappers, repository implementations
- `domain`: models, repository interfaces, business rules. No Android, Retrofit, Room or Compose imports.
- `presentation`: screens, ViewModels, UI state

## Running the tests

```
./gradlew test
```

TODO: summary of the automated tests.

## Known limitations

TODO
