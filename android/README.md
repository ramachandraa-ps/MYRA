# My RA Friend - Android Application

## Overview
This is the native Android application for My RA Friend - a comprehensive Rheumatoid Arthritis management platform. The app is built using Java with MVVM architecture and follows Android best practices.

## Architecture

### MVVM Pattern
- **Model**: Data models and entities (`models/`, `database/entities/`)
- **View**: Activities, Fragments, and XML layouts (`ui/`, `res/layout/`)
- **ViewModel**: Business logic layer (`viewmodels/`)
- **Repository**: Data source abstraction (`repository/`)

## Technology Stack

### Core Technologies
- **Language**: Java
- **Min SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Architecture**: MVVM (Model-View-ViewModel)

### Key Libraries
- **Networking**: Retrofit 2.9.0 + OkHttp 4.12.0
- **Database**: Room 2.6.1 (offline caching)
- **Firebase**: Firebase BOM 32.7.0 (Cloud Messaging)
- **Image Loading**: Glide 4.16.0
- **Charts**: MPAndroidChart v3.1.0
- **UI**: Material Design Components

## Project Structure

```
android/
├── app/src/main/
│   ├── java/com/myrafriend/
│   │   ├── database/
│   │   │   ├── entities/          # Room entities
│   │   │   ├── dao/               # Data Access Objects
│   │   │   └── AppDatabase.java   # Database instance
│   │   ├── models/                # Data models
│   │   ├── network/               # API service & Retrofit
│   │   ├── repository/            # Repository layer
│   │   ├── viewmodels/            # ViewModels
│   │   ├── ui/                    # Activities & Fragments
│   │   ├── services/              # FCM service
│   │   └── utils/                 # Utility classes
│   ├── res/
│   │   ├── layout/                # XML layouts
│   │   ├── values/                # Strings, colors, etc.
│   │   ├── menu/                  # Navigation menus
│   │   └── drawable/              # Graphics
│   └── AndroidManifest.xml
└── build.gradle
```

## Implemented Features

### 1. Authentication
- **LoginActivity**: Email/password authentication with JWT tokens
- **SplashActivity**: Auto-login functionality
- Token management with SharedPreferences
- Secure token storage and automatic injection via OkHttp interceptor

### 2. Patient Features

#### Dashboard (PatientDashboardFragment)
- Welcome card with user information
- Quick action cards for:
  - Log Symptoms
  - View Medications
  - Access Rehab Exercises
  - Check Messages
- Recent activity summary

#### Symptom Logging (SymptomLogFragment)
- Pain level tracking (VAS 0-10)
- Joint count assessment (DAS28 0-28)
- Morning stiffness duration
- Fatigue level (FACIT 0-10)
- Swelling and warmth indicators
- Functional ability assessment
- Notes field
- **Flare reporting** to doctor with severity levels

#### Medications (MedicationListFragment)
- View assigned medications
- Medication details (dosage, frequency, timing)
- Mark medications as taken/skipped
- Adherence tracking with percentage display
- Offline support with Room database

#### Rehabilitation (RehabListFragment)
- View assigned exercises
- Mark exercises as complete
- Progress tracking

#### Messaging (MessagesFragment)
- Communication with assigned doctor
- Message history
- File attachment support

### 3. Network Layer

#### RetrofitClient
- Singleton pattern implementation
- Automatic JWT token injection
- Logging interceptor for debugging
- Token and user data management
- 30+ API endpoints configured

#### API Service
Complete REST API integration including:
- Authentication (login, token refresh, FCM token update)
- Patient operations (symptom logs, medications, rehab, lab reports)
- Doctor operations (patient management, prescribing, assignments)
- Messaging and appointments

### 4. Offline Support

#### Room Database
- **SymptomLogEntity**: Cached symptom logs
- **MedicationEntity**: Cached medication assignments
- **MedicationIntakeEntity**: Cached intake logs
- Sync status tracking for offline operations

#### DAOs (Data Access Objects)
- SymptomLogDao: CRUD operations for symptoms
- MedicationDao: Medication management
- MedicationIntakeDao: Intake log tracking
- LiveData support for reactive UI updates

### 5. Repositories
- **AuthRepository**: Authentication operations
- **SymptomRepository**: Symptom logging with offline-first approach
- **MedicationRepository**: Medication management with caching
- **DoctorRepository**: Doctor-specific operations
- **MessageRepository**: Messaging and appointments
- Resource wrapper pattern for clean state management (SUCCESS, ERROR, LOADING)

### 6. ViewModels
- **LoginViewModel**: Authentication state management
- **SymptomViewModel**: Symptom logging operations
- **MedicationViewModel**: Medication tracking
- **DoctorViewModel**: Doctor operations
- **MessageViewModel**: Messaging and appointments

### 7. Utility Classes

#### ValidationUtils
- Email format validation
- Password strength checking
- Pain level (0-10) validation
- Joint count (0-28) validation
- Required field validation

#### DateTimeUtils
- Date/time formatting
- Relative time strings ("2 hours ago")
- Date range calculations
- Multiple format support

#### NetworkUtils
- Network connectivity checking
- WiFi/mobile data detection
- Network type identification

#### NotificationHelper
- Notification channel management
- Medication reminders
- Appointment reminders
- Message notifications
- Flare alerts for doctors

### 8. Firebase Cloud Messaging

#### MyRAFriendMessagingService
- Push notification handling
- FCM token management
- Notification type handling:
  - Medication reminders
  - Missed dose alerts
  - Flare alerts
  - New messages
  - Appointment reminders
  - Medication/rehab assignments

### 9. UI Components

#### Material Design
- MaterialButton, MaterialCardView
- TextInputLayout with outline style
- Material sliders for pain/fatigue levels
- BottomNavigationView with custom styling
- MaterialCheckBox

#### Layouts
- **activity_login.xml**: Login screen with Material Design
- **activity_splash.xml**: Splash screen
- **activity_main.xml**: Main container with bottom navigation
- **fragment_patient_dashboard.xml**: Dashboard with quick actions
- **fragment_symptom_log.xml**: Comprehensive symptom logging form
- **fragment_medication_list.xml**: Medication list with adherence tracking
- **item_medication.xml**: Medication card layout

### 10. Configuration

#### AndroidManifest.xml
- All required permissions (Internet, Network State, Storage, Notifications)
- Activity declarations (Splash, Login, Main)
- FCM service configuration
- Network security config
- Firebase metadata

#### Network Security
- Cleartext traffic allowed for local development (localhost, 10.0.2.2)
- HTTPS enforced for production
- System certificate trust

## API Integration

### Base URL Configuration
```java
// Development
private static final String BASE_URL = "http://10.0.2.2/myrafriend/";

// Production
private static final String BASE_URL = "https://api.myrafriend.com/";
```

### Authentication Flow
1. User enters email/password
2. LoginActivity sends credentials to `/auth/login`
3. Server returns JWT token and user data
4. Token stored in SharedPreferences
5. OkHttp interceptor automatically adds token to all subsequent requests
6. RetrofitClient manages token lifecycle

### Offline-First Approach
1. User performs action (e.g., log symptom)
2. Data saved to Room database immediately
3. Background sync attempts to send to server
4. Success: Mark as synced
5. Failure: Keep in local database for retry

## Building and Running

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 11 or later
- Android SDK API 34
- Firebase project configured (google-services.json)

### Setup Steps
1. Clone the repository
2. Open project in Android Studio
3. Add `google-services.json` to `app/` directory
4. Configure API base URL in `RetrofitClient.java`
5. Sync Gradle dependencies
6. Build and run

### Build Variants
```gradle
buildTypes {
    debug {
        buildConfigField "String", "API_BASE_URL", "\"http://10.0.2.2/myrafriend/\""
    }
    release {
        buildConfigField "String", "API_BASE_URL", "\"https://api.myrafriend.com/\""
        minifyEnabled true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```

## Security Features

### Authentication
- JWT token-based authentication
- 24-hour token expiration
- Secure token storage in SharedPreferences
- Automatic token injection via interceptor

### Data Protection
- HTTPS for all production traffic
- Network security config
- No sensitive data in logs (production)
- Account lockout after 3 failed attempts (backend)

### Permissions
- Runtime permissions for Android 6.0+
- Minimal permission requests
- Storage permissions for lab reports only

## Testing

### Test Credentials
- **Patient**: patient1@test.com / Test@123
- **Doctor**: dr.smith@myrafriend.com / Test@123
- **Admin**: admin@myrafriend.com / Test@123

### Testing Checklist
- [ ] Login with valid credentials
- [ ] Auto-login after app restart
- [ ] Submit symptom log
- [ ] View medications
- [ ] Mark medication as taken
- [ ] Report flare to doctor
- [ ] Receive push notifications
- [ ] Test offline mode (airplane mode)
- [ ] Logout functionality

## Known Limitations

### Current Implementation
- RecyclerView adapters not yet implemented (medication list, messages)
- Doctor screens placeholder only
- Chart visualization pending
- Video playback for rehab exercises not implemented
- File upload for lab reports pending
- Real-time messaging not implemented

### Future Enhancements
1. Complete RecyclerView adapters
2. Implement doctor patient management screens
3. Add MPAndroidChart for symptom trends
4. Implement video player for rehab exercises
5. Add lab report upload functionality
6. Real-time messaging with WebSocket
7. Local notifications for medication reminders
8. Biometric authentication support
9. Dark mode theme
10. Accessibility improvements

## Dependencies

### Gradle Dependencies
```gradle
// Networking
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:okhttp:4.12.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'

// Room Database
implementation 'androidx.room:room-runtime:2.6.1'
annotationProcessor 'androidx.room:room-compiler:2.6.1'

// Firebase
implementation platform('com.google.firebase:firebase-bom:32.7.0')
implementation 'com.google.firebase:firebase-messaging'
implementation 'com.google.firebase:firebase-analytics'

// Material Design
implementation 'com.google.android.material:material:1.11.0'

// Image Loading
implementation 'com.github.bumptech.glide:glide:4.16.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.16.0'

// Charts
implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'

// Lifecycle Components
implementation 'androidx.lifecycle:lifecycle-viewmodel:2.7.0'
implementation 'androidx.lifecycle:lifecycle-livedata:2.7.0'
```

## Support

For issues or questions:
- GitHub Issues: https://github.com/myrafriend/android
- Email: support@myrafriend.com

## License

Copyright © 2024 My RA Friend. All rights reserved.

---

**Note**: This application requires the backend PHP API to be running. See the main project README for backend setup instructions.
