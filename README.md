# 🎵 Moises Android Challenge

**Moises Android Challenge** is a modern Android application that allows you to explore, search, and play songs using the [iTunes API](https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/index.html). The project was developed with a focus on current technologies, following the best practices for Android development.

---

## 📸 Screenshots

<div>
  <img src="screenshots/screenshot_01.png" width="30%" alt="screenshot" />
  <img src="screenshots/screenshot_02.png" width="30%" alt="screenshot" />
  <img src="screenshots/screenshot_03.png" width="30%" alt="screenshot" />
  <img src="screenshots/screenshot_04.png" width="30%" alt="screenshot" />
  <img src="screenshots/screenshot_05.png" width="30%" alt="screenshot" />
  <img src="screenshots/screenshot_06.png" width="30%" alt="screenshot" />
</div>

<div>
  <img src="screenshots/landscape_screenshot.png" width="100%" alt="screenshot" />
</div>

---

## 🏗️ Architecture

The project uses the **MVVM (Model-View-ViewModel)** architecture combined with **Clean Architecture** principles, ensuring a clear separation of concerns, ease of maintenance, and testability.

- **Data**: Implementation of repositories, data sources (Remote and Local), mapping of data models (DTOs), and media player controller.
- **Domain**: Business rules, domain models, and Use Cases.
- **Presentation**: Declarative UI with Jetpack Compose, ViewModels for state management, and UI State.
- **Core**: Shared components, design system, utilities, and base classes for other layers.

<div>
  <img src="screenshots/architecture.png" width="100%" alt="Architecture Diagram" />
</div>

---

## 🧠 SOLID Principles

The codebase strictly adheres to **SOLID** principles to maximize testability, maintainability, and loose coupling:

- **S — Single Responsibility Principle (SRP)**
  - **Use Cases**: Individual business rules are encapsulated into focused classes like [`SearchSongsUseCase`](app/src/main/java/dev/brunofelix/moiseschallenge/feature/song/domain/use_case/SearchSongsUseCase.kt) (search logic) and [`SaveRecentSongUseCase`](app/src/main/java/dev/brunofelix/moiseschallenge/feature/song/domain/use_case/SaveRecentSongUseCase.kt) (song persistence).
  - **Mappers**: Dedicated mappers like [`SongEntityMapper`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/local/mapper/SongEntityMapper.kt) and [`SongDtoMapper`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/remote/mapper/SongDtoMapper.kt) handle model transformations between data layers.
  - **Data Sources**: [`RoomLocalDataSourceImpl`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/local/source/RoomLocalDataSourceImpl.kt) exclusively manages local SQLite database storage, while [`ITunesRemoteDataSourceImpl`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/remote/source/ITunesRemoteDataSourceImpl.kt) handles network requests.
  - **Player Controller**: [`ExoPlayerControllerImpl`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/player/ExoPlayerControllerImpl.kt) isolates low-level audio playback and state tracking from ViewModels and UI.

- **O — Open/Closed Principle (OCP)**
  - **Interfaces**: Domain contracts like [`PlayerController`](app/src/main/java/dev/brunofelix/moiseschallenge/core/domain/player/PlayerController.kt) and [`SongRepository`](app/src/main/java/dev/brunofelix/moiseschallenge/core/domain/repository/SongRepository.kt) allow swapping or extending underlying implementations (e.g., mock engines, background services) without modifying existing ViewModels or UI components.
  - **Sealed Interfaces for State**: [`UiState`](app/src/main/java/dev/brunofelix/moiseschallenge/core/presentation/util/UiState.kt) enables adding new UI states (e.g., `Initial`, `Loading`, `Success`, `Error`, `Empty`) without altering existing state-handling contracts.

- **L — Liskov Substitution Principle (LSP)**
  - **Substitutability in Testing**: Concrete implementations like [`SongRepositoryImpl`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/repository/SongRepositoryImpl.kt) can be replaced seamlessly with test doubles or mocks in unit tests (e.g., [`SongViewModelTest`](app/src/test/java/dev/brunofelix/moiseschallenge/feature/song/presentation/SongViewModelTest.kt)) while upholding the contract expectations.

- **I — Interface Segregation Principle (ISP)**
  - **Granular Domain Interfaces**: Domain repositories are split by feature domain ([`SongRepository`](app/src/main/java/dev/brunofelix/moiseschallenge/core/domain/repository/SongRepository.kt) and [`AlbumRepository`](app/src/main/java/dev/brunofelix/moiseschallenge/core/domain/repository/AlbumRepository.kt)) rather than being merged into a monolithic repository.
  - **Targeted Data Sources**: [`SongLocalDataSource`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/local/source/SongLocalDataSource.kt) defines local database operations, while [`SongRemoteDataSource`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/remote/source/SongRemoteDataSource.kt) defines network calls.
  - **Specific ViewModel Injections**: [`SongViewModel`](app/src/main/java/dev/brunofelix/moiseschallenge/feature/song/presentation/SongViewModel.kt) injects only the specific Use Cases it requires (`SearchSongsUseCase`, `SaveRecentSongUseCase`, `GetRecentlyPlayedSongsUseCase`).

- **D — Dependency Inversion Principle (DIP)**
  - **Clean Architecture Boundaries**: The high-level Domain layer defines abstractions (`SongRepository`, `PlayerController`) with zero dependencies on frameworks like Room, Retrofit, or ExoPlayer.
  - **Dependency Injection**: [`RepositoryModule`](app/src/main/java/dev/brunofelix/moiseschallenge/core/di/RepositoryModule.kt) and [`PlayerControllerModule`](app/src/main/java/dev/brunofelix/moiseschallenge/core/di/PlayerControllerModule.kt) use Hilt `@Binds` annotations to bind domain interfaces to their concrete data layer implementations.

---

## 🧩 Design Patterns

Beyond SOLID, the project leverages key **Gang of Four (GoF)** and modern Android design patterns:

- **Facade Pattern**
  - [`ExoPlayerControllerImpl`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/player/ExoPlayerControllerImpl.kt): Provides a unified, simplified facade ([`PlayerController`](app/src/main/java/dev/brunofelix/moiseschallenge/core/domain/player/PlayerController.kt)) over the complex Media3 ExoPlayer API (listeners, position tracking, buffering states, repeat mode).
  - [`BaseRemoteDataSource`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/util/BaseRemoteDataSource.kt): Abstracts Retrofit execution (`safeApiCall`), error mapping, and thread context switching (`Dispatchers.IO`).
  - [`SongRepositoryImpl`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/repository/SongRepositoryImpl.kt): Acts as a facade over local and remote data sources for the domain layer.

- **Adapter Pattern**
  - [`LocalPagingSource`](app/src/main/java/dev/brunofelix/moiseschallenge/core/presentation/util/LocalPagingSource.kt): Adapts an in-memory `List<T>` to Jetpack Paging 3's `PagingSource<Int, T>` API.
  - [`RoomLocalDataSourceImpl`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/local/source/RoomLocalDataSourceImpl.kt): Adapts Room DAO methods (`SongDao`) to the `SongLocalDataSource` domain contract.
  - [`ITunesRemoteDataSourceImpl`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/remote/source/ITunesRemoteDataSourceImpl.kt): Adapts Retrofit API endpoints (`ITunesApi`) to `SongRemoteDataSource`.

- **Data Mapper Pattern**
  - [`SongEntityMapper`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/local/mapper/SongEntityMapper.kt) & [`SongDtoMapper`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/remote/mapper/SongDtoMapper.kt): Decouple database entities (`SongEntity`) and network DTOs (`SongDto`) from core Domain models (`Song`).

- **Observer Pattern / Reactive Streams**
  - **Coroutines Flow & StateFlow**: Reactive data pipelines streaming changes end-to-end (`DAO` -> `DataSource` -> `Repository` -> `ViewModel` -> `Jetpack Compose UI`).
  - **Player Listener**: Subscribes to ExoPlayer playback events in [`ExoPlayerControllerImpl`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/player/ExoPlayerControllerImpl.kt) to update player states reactively.

- **Strategy / Use Case Pattern**
  - Encapsulates discrete business algorithms using Kotlin `operator fun invoke(...)`: [`SearchSongsUseCase`](app/src/main/java/dev/brunofelix/moiseschallenge/feature/song/domain/use_case/SearchSongsUseCase.kt), [`GetRecentlyPlayedSongsUseCase`](app/src/main/java/dev/brunofelix/moiseschallenge/feature/song/domain/use_case/GetRecentlyPlayedSongsUseCase.kt), and [`GetAlbumByIdUseCase`](app/src/main/java/dev/brunofelix/moiseschallenge/feature/album/domain/use_case/GetAlbumByIdUseCase.kt).

- **State Pattern & Unidirectional Data Flow (UDF / MVI)**
  - Sealed interfaces [`UiState`](app/src/main/java/dev/brunofelix/moiseschallenge/core/presentation/util/UiState.kt) (`Initial`, `Loading`, `Success`, `Error`, `Empty`) and `PlayerState` (`Idle`, `Buffering`, `Playing`, `Paused`, `Ended`, `Error`) enforce explicit, immutable state transitions.

- **Template Method Pattern**
  - [`BaseRemoteDataSource`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/util/BaseRemoteDataSource.kt): Defines the skeleton algorithm for safe API calls (`safeApiCall`), delegating the specific request lambdas and transformations to subclasses.

- **Dependency Injection & Factory Method**
  - Managed by **Hilt (Dagger)**. Factory `@Provides` methods in [`PlayerControllerModule`](app/src/main/java/dev/brunofelix/moiseschallenge/core/di/PlayerControllerModule.kt) construct complex dependencies (`ExoPlayer`, `CacheDataSource.Factory`, `SimpleCache`).

- **Repository Pattern**
  - [`SongRepositoryImpl`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/repository/SongRepositoryImpl.kt) & [`AlbumRepositoryImpl`](app/src/main/java/dev/brunofelix/moiseschallenge/core/data/repository/AlbumRepositoryImpl.kt) encapsulate access to local and remote data sources, acting as an in-memory collection interface for domain models.

---

## 🛠️ Technologies and Dependencies

### Environment & Configuration
- **Android SDK**: `compileSdk 37`, `minSdk 26`, `targetSdk 36`
- **Java**: Version 21
- **Gradle**: Android Gradle Plugin (AGP) 9.3.1 (Gradle 9.5.0)

### Core
- **Kotlin**: Modern and concise programming language.
- **Coroutines & Flow**: Asynchronous programming and reactive data streams.
- **Hilt (Dagger)**: Dependency injection for Android.

### UI & UX
- **Jetpack Compose**: Modern toolkit for building native UI.
- **Material 3**: Google's design system for modern interfaces.
- **Navigation 3**: Type-safe and decoupled navigation between screens.
- **Coil**: Efficient image loading optimized for Compose.
- **Splash Screen API**: Native support for splash screens.

### Data, Media & Networking
- **Retrofit & OkHttp**: REST API consumption and HTTP request management.
- **Room**: Local database (SQLite) with robust abstraction.
- **Paging 3**: Efficient data pagination from API and local database.
- **Media3 (ExoPlayer)**: Powerful and extensible media player for audio playback.
- **Kotlinx Serialization**: JSON data serialization.

---

## 📝 Commit Patterns

The project adopts the **Conventional Commits** standard in conjunction with **Gitmojis** to maintain a readable and organized change history.

### Commit Types:
- `✨ feat`: Introduction of new features.
- `🐛 fix`: Bug fixes.
- `♻️ refactor`: Code changes that neither fix bugs nor add features.
- `✅ test`: Adding or correcting tests.
- `🔧 chore`: Maintenance tasks, build configurations, refactoring structures, etc.
- `📦 build`: Changes affecting the build system or external dependencies.
- `📝 docs`: Documentation changes.
- `🎨 style`: Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc).

### Example:
`✨ feat(song): add delay to recentlyPlayedSongs flow`

---

## 🌿 Branching Strategy (Gitflow)

The project's development followed a structured branching model inspired by **Gitflow** to organize the implementation phases. The following branches were used throughout the project:

- `setup/project`: Initial project configuration, dependencies and structure.
- `setup/design-system`: Definition of typography, colors, shapes, and base components.
- `feature/domain`: Implementation of models, repositories interfaces, and use cases.
- `feature/data`: Implementation of local/remote data sources, DTOs, and mappers.
- `feature/presentation`: Development of the UI using Jetpack Compose and ViewModels.
- `fix/ui-tweaks`: Adjustments and bug fixes in the user interface and responsiveness.
- `test/unit-tests`: Addition of unit tests for the domain, data, and presentation layers.
- `test/ui-tests`: Implementation of UI and integration tests.

---

## 🧪 Testing
The project has a solid testing foundation to ensure code quality:
- **Unit Tests**: JUnit 4, Kotest, MockK, Mockito, and Truth.
- **Network Tests**: MockWebServer for mocking API responses.
- **Integration Tests**: Robolectric for testing Android framework on the JVM.
- **UI Tests**: Espresso and Compose UI Test.

---

## 🚀 How to Run

1. Clone the repository.
2. Open the project in Android Studio.
3. Sync Gradle and build the project.
4. Run the application on an emulator or physical device.

---

## 📥 Download APK

You can download the latest version of the application using the link below:

[![Download APK](https://img.shields.io/badge/Download-APK-green?style=for-the-badge&logo=android)](https://github.com/brunofelix7/moises-android-challenge/releases/download/v1.0.0/app-release.apk)

---

Developed by [Bruno Felix](https://github.com/brunofelix7) 🚀
