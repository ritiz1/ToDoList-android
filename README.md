# ToDo List Android App

A modern Android application for managing your notes and tasks with JWT authentication. Built with Jetpack Compose, Kotlin, and Ktor client for seamless backend integration with a Django REST Framework API.

## 📋 Features

- **User Authentication**: Login with username and password using JWT tokens
- **Token Management**: Secure token storage using DataStore (modern Android approach)
- **Note Management**: 
  - View all notes
  - Create new notes with title and body
  - Automatic token handling for authenticated requests
- **Modern UI**: Built with Jetpack Compose for a responsive, declarative UI
- **Automatic Session Management**: App remembers login state and redirects appropriately

## 🏗️ Architecture

### Tech Stack

- **Frontend**: Android with Jetpack Compose
- **HTTP Client**: Ktor Client
- **Authentication**: JWT (JSON Web Tokens)
- **Data Storage**: DataStore (Preferences)
- **State Management**: ViewModel + StateFlow
- **Navigation**: Jetpack Compose Navigation

### Project Structure

```
app/src/main/java/com/example/todolist/
├── backend/
│   └── APIservice.kt          # HTTP client configuration & API calls
├── frontend/
│   ├── LoginScreen.kt         # Login UI
│   ├── HomeScreen.kt          # Notes list display
│   ├── CreatePost.kt          # Create note UI
│   └── AfterLoggedIn.kt       # Post-login screen with logout
├── models/
│   └── Models.kt              # Data classes (Note, TokenResponse, LoginRequest)
├── viewModels/
│   ├── LoginScreenViewModel.kt
│   ├── HomeScreenViewModel.kt
│   └── CreatePostViewModel.kt
├── utils/
│   └── TokenManager.kt        # Secure token storage & retrieval
├── ui/
│   └── theme/                 # Material 3 theme configuration
├── MainActivity.kt            # Entry point & Navigation setup
└── Destinations.kt            # Route definitions
```

## 🔐 Backend Integration

### Base URL
```
http://10.0.2.2:8000/api/
```
(Uses `10.0.2.2` for Android Emulator connectivity to local Django server)

### Authentication
- **Header Required**: `Authorization: Bearer <access_token>`
- **Access Token Lifespan**: 30 days (dev configuration)

### API Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---|
| POST | `/token/` | Login with username & password | ❌ |
| POST | `/register/` | Register new user | ❌ |
| GET | `/notes/` | Fetch all user notes | ✅ |
| POST | `/notes/` | Create a new note | ✅ |
| GET | `/notes/{id}/` | Get a single note | ✅ |
| PUT | `/notes/{id}/` | Update a note | ✅ |
| DELETE | `/notes/{id}/` | Delete a note | ✅ |

### Request/Response Models

**LoginRequest**
```json
{
  "username": "user1",
  "password": "secretpassword"
}
```

**TokenResponse**
```json
{
  "access": "eyJ0eXAiOiJKV1QiLCJhbGc...",
  "refresh": "eyJ0eXAiOiJKV1QiLCJhbGc..."
}
```

**Note Object**
```json
{
  "id": 1,
  "title": "Buy Milk",
  "body": "Don't forget the almond milk",
  "created_at": "2024-12-17T10:00:00Z"
}
```

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest version)
- Android SDK 27+ (minSdk)
- Gradle 8.0+
- Django REST Framework backend running on `http://localhost:8000`

### Installation

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd ToDoList
   ```

2. **Open in Android Studio**
   - Open `C:\Users\adhik\ToDoList` as a project

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run on Android Emulator**
   - Create/select an AVD in Android Studio
   - Click "Run" or press `Shift + F10`

### Backend Setup

Ensure your Django REST Framework backend is running:

```bash
# In your Django project directory
python manage.py runserver 0.0.0.0:8000
```

## 📱 User Flow

```
Launch App
    ↓
[Token exists?]
    ├─ YES → Home Screen (Notes List)
    └─ NO  → Login Screen
         ↓
    [Enter Credentials]
         ↓
    [Login API Call]
         ↓
    [Token Saved to DataStore]
         ↓
    Home Screen
         ↓
    [View / Create Notes]
         ↓
    [Logout] → Clear Token → Login Screen
```

## 🔑 Key Components

### APIservice.kt
- **HttpClient Configuration**: Sets up Ktor client with JSON serialization
- **login()**: Authenticates user and returns JWT tokens
- **getNotes()**: Fetches all notes with authorization header
- **createNote()**: Creates new note (with token validation)

### TokenManager.kt
- Stores access token securely in DataStore
- Retrieves token as StateFlow for reactive updates
- Handles token clearing on logout

### ViewModels
- **LoginScreenViewModel**: Handles login logic and state
- **HomeScreenViewModel**: Manages note fetching and display
- **CreatePostViewModel**: Handles note creation

### AppNavigation
- Checks for existing token on app launch
- Routes to Home if authenticated, Login otherwise
- Manages back stack and navigation transitions

## ⚙️ Dependencies

```kotlin
// Navigation
androidx.navigation:navigation-compose:2.7.7

// Data Storage
androidx.datastore:datastore-preferences:1.0.0

// Lifecycle & Compose
androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0

// Ktor Client
io.ktor:ktor-client-core:2.3.7
io.ktor:ktor-client-android:2.3.7
io.ktor:ktor-client-content-negotiation:2.3.7
io.ktor:ktor-serialization-kotlinx-json:2.3.7
io.ktor:ktor-client-logging:2.3.7

// Kotlin Serialization
org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2

// Material Design 3
androidx.material3:material3
```

## 🛠️ Development Notes

### Token Storage
The app uses Android DataStore (Preferences) to securely store JWT access tokens. This is the modern recommended approach replacing SharedPreferences.

### Content Negotiation
Ktor client is configured with:
- **ignoreUnknownKeys**: True (API may return extra fields)
- **isLenient**: True (flexible JSON parsing)
- **ContentNegotiation**: Automatic JSON serialization/deserialization

### Error Handling
- Login failures display error messages
- Network errors are caught and displayed to user
- Unauthorized responses (401) on API calls indicate token expiration

## 🐛 Troubleshooting

### "Fail to prepare request body for sending"
- Ensure `ContentNegotiation` is installed in HttpClient
- Verify `@Serializable` annotation on data classes
- Check that request body is set with `.setBody()`

### 401 Unauthorized on API calls
- Token may have expired
- Verify token is being passed in Authorization header correctly
- Check backend JWT configuration

### Cannot connect to backend
- Ensure Django server is running on `localhost:8000`
- Verify emulator can reach `10.0.2.2` (Android's way of calling host localhost)
- Check network permissions in AndroidManifest.xml

## 📝 Future Enhancements

- [ ] Token refresh functionality
- [ ] Edit existing notes
- [ ] Delete notes
- [ ] Search notes functionality
- [ ] Offline mode with local caching
- [ ] Note categories/tags
- [ ] Dark mode support
- [ ] Unit and integration tests

## 📄 License

This project is open source and available under the MIT License.

## 👤 Author

Created as a learning project for Android development with Kotlin and Jetpack Compose.

---

**Last Updated**: December 2025  
**Status**: Active Development

