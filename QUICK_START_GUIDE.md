# My RA Friend - Quick Start Guide

## 🎯 What's Been Built

### ✅ Complete PHP Backend API (100%)
- 30+ REST API endpoints
- JWT authentication with account lockout
- Role-based access control
- File upload handling
- FCM push notifications
- Complete database schema
- Test data ready

### 🚧 Android Project Structure (10%)
- Project directories created
- build.gradle configured with all dependencies
- Ready for implementation

## 🚀 Get Started in 5 Minutes

### Step 1: Set Up Database

```bash
cd "/mnt/d/Mobile Apps Native/MYRA/backend"

# Import database (requires MySQL installed)
mysql -u root -p
# Then run:
source database/schema.sql
source database/master_data.sql
source database/test_users.sql
exit
```

### Step 2: Configure Backend

1. Edit `backend/config/database.php`:
```php
private $password = "your_mysql_password";
```

2. Edit `backend/config/constants.php`:
```php
define('FCM_SERVER_KEY', 'get_from_firebase_console');
```

3. Set permissions:
```bash
chmod 777 backend/uploads/
chmod 777 backend/logs/
```

### Step 3: Test Backend API

```bash
# Test login endpoint
curl -X POST http://localhost/backend/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "patient1@test.com",
    "password": "Test@123"
  }'

# You should get a JWT token in response
```

### Step 4: Set Up Android Project

1. Open Android Studio
2. Click "Open an Existing Project"
3. Navigate to: `/mnt/d/Mobile Apps Native/MYRA/android/`
4. Wait for Gradle sync

5. Add Firebase configuration:
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create new project "My RA Friend"
   - Add Android app with package name: `com.myrafriend`
   - Download `google-services.json`
   - Place it in: `android/app/google-services.json`

6. Update API URL in `app/build.gradle` if needed:
```gradle
buildConfigField "String", "API_BASE_URL", "\"http://YOUR_IP/backend/\""
```

## 📱 Test Accounts

### Admin
- Email: `admin@myrafriend.com`
- Password: `Test@123`

### Doctor
- Email: `dr.smith@myrafriend.com`
- Password: `Test@123`

### Patient (Use this for testing)
- Email: `patient1@test.com`
- Password: `Test@123`

## 📋 Backend Features Ready to Use

All these endpoints are fully functional:

### Authentication
- `POST /auth/login` - Login
- `POST /auth/refresh` - Refresh token
- `POST /auth/update-fcm-token` - Update FCM token

### Patient Features
- `POST /patient/symptom-logs` - Log symptoms
- `GET /patient/symptom-logs/{id}` - Get history
- `POST /patient/flare-report` - Report flare
- `GET /patient/medications/{id}` - Get medications
- `POST /patient/medication-intake` - Record intake
- `GET /patient/rehab/{id}` - Get exercises
- `POST /patient/rehab-completion` - Mark complete
- `POST /patient/lab-reports` - Upload lab report
- `GET /patient/lab-reports/{id}` - Get reports

### Doctor Features
- `GET /doctor/patients/{doctor_id}` - List patients
- `GET /doctor/patient-detail/{patient_id}` - Patient details
- `POST /doctor/prescribe-medication` - Prescribe
- `POST /doctor/assign-rehab` - Assign exercise
- `GET /doctor/medications-master` - Get medication list
- `GET /doctor/rehab-exercises-master` - Get exercise list

### Messaging & Appointments
- `GET /messages` - Get messages
- `POST /messages/send` - Send message
- `GET /appointments` - Get appointments
- `POST /appointments/book` - Book appointment

## 🔍 API Testing with Postman

### Import Postman Collection

Create a new collection with these requests:

**1. Login (No Auth Required)**
```
POST http://localhost/backend/auth/login
Body (JSON):
{
  "email": "patient1@test.com",
  "password": "Test@123"
}
```

**2. Submit Symptom Log (Requires Auth)**
```
POST http://localhost/backend/patient/symptom-logs
Headers:
  Authorization: Bearer YOUR_JWT_TOKEN
Body (JSON):
{
  "pain_level": 7,
  "joint_count": 12,
  "morning_stiffness": "30_minutes",
  "fatigue_level": 6,
  "swelling": true,
  "warmth": false,
  "functional_ability": "moderate",
  "notes": "Increased pain after morning activity"
}
```

**3. Get Symptom History**
```
GET http://localhost/backend/patient/symptom-logs/1
Headers:
  Authorization: Bearer YOUR_JWT_TOKEN
```

## 🏗️ Android Development - Next Steps

### Priority 1: Core Data Models

Create in `android/app/src/main/java/com/myrafriend/models/`:

1. `User.java`
2. `SymptomLog.java`
3. `Medication.java`
4. `RehabExercise.java`
5. `LabReport.java`
6. `Message.java`
7. `Appointment.java`

### Priority 2: Network Layer

Create in `android/app/src/main/java/com/myrafriend/network/`:

1. `ApiService.java` - Interface with all endpoints
2. `RetrofitClient.java` - Singleton instance
3. `AuthInterceptor.java` - Add JWT to requests

### Priority 3: Repository Layer

Create in `android/app/src/main/java/com/myrafriend/repository/`:

1. `AuthRepository.java`
2. `SymptomRepository.java`
3. `MedicationRepository.java`

### Priority 4: ViewModels

Create in `android/app/src/main/java/com/myrafriend/viewmodels/`:

1. `LoginViewModel.java`
2. `SymptomLogViewModel.java`
3. `MedicationViewModel.java`

### Priority 5: UI Screens

Create in `android/app/src/main/res/layout/`:

1. `activity_login.xml`
2. `activity_main.xml`
3. `fragment_symptom_log.xml`
4. `fragment_medication_list.xml`

## 🎨 Design Guidelines

### Colors (colors.xml)
```xml
<color name="primary_teal">#008C95</color>
<color name="secondary_blue">#5EA8DC</color>
<color name="background_white">#FFFFFF</color>
<color name="text_dark">#2F2F2F</color>
<color name="border_gray">#E6E6E6</color>
<color name="error_red">#D9534F</color>
<color name="success_green">#4CAF50</color>
```

### Typography
- Use Sans-serif or Roboto
- Large headings (20-24sp)
- Body text (16sp)
- High contrast for readability

## 📚 Documentation

- **Implementation Status**: `IMPLEMENTATION_STATUS.md`
- **Backend README**: `backend/README.md`
- **Product Requirements**: `PRD.md`
- **Design Specification**: `.kiro/specs/my-ra-friend/design.md`
- **Requirements**: `.kiro/specs/my-ra-friend/requirements.md`
- **Task List**: `.kiro/specs/my-ra-friend/tasks.md`

## ⚠️ Common Issues & Solutions

### Issue: Database connection failed
**Solution**: Check MySQL is running and credentials in `config/database.php`

### Issue: 404 on API endpoints
**Solution**: Ensure .htaccess rewrite rules are enabled (Apache) or nginx configured

### Issue: CORS errors
**Solution**: CORS headers are already set in index.php - check browser console

### Issue: File upload fails
**Solution**: Check directory permissions (chmod 777) on uploads/

### Issue: JWT token invalid
**Solution**: Token expires after 24 hours - use refresh endpoint or login again

## 🎯 Recommended Development Flow

### Week 1: Core Android Setup
- Day 1-2: Data models and network layer
- Day 3-4: Repository and ViewModel for auth + symptoms
- Day 5: Login screen and symptom logging UI

### Week 2: Patient Features
- Day 1-2: Medication management UI
- Day 3-4: Rehab exercises UI
- Day 5: Lab reports and messaging

### Week 3: Doctor Features
- Day 1-2: Doctor dashboard and patient list
- Day 3-4: Patient detail and prescription UI
- Day 5: Testing and bug fixes

### Week 4: Polish & Testing
- Day 1-2: Firebase notifications
- Day 3-4: Offline sync
- Day 5: Integration testing

## 🔗 Useful Links

- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging)
- [Material Design](https://material.io/design)
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)

## 💬 Support

For questions about:
- **Backend API**: Check `backend/README.md`
- **Requirements**: Check `PRD.md` and `requirements.md`
- **Design Specs**: Check `design.md`

## ✅ Checklist Before Starting Android Development

- [ ] MySQL database imported successfully
- [ ] Backend API responding to requests
- [ ] Test login returns JWT token
- [ ] Firebase project created
- [ ] google-services.json downloaded
- [ ] Android Studio project opens without errors
- [ ] Gradle sync completes successfully
- [ ] API_BASE_URL configured correctly

---

**You're Ready to Build!** 🚀

Start with the models, then network layer, then UI. The backend is complete and waiting for your Android app to connect to it!
