# My RA Friend - Build & Run Instructions

## 🚀 Quick Start (10 Minutes)

### Prerequisites
- ✅ XAMPP/WAMP with PHP 7.4+ and MySQL 8.0+
- ✅ Android Studio Arctic Fox or later
- ✅ JDK 11 or later
- ✅ Android SDK API 34
- ✅ Firebase project (for push notifications)

---

## 📦 Backend Setup (5 Minutes)

### Step 1: Start Services
```bash
# Start XAMPP Control Panel
# Start Apache and MySQL services
```

### Step 2: Create Database
```bash
# Open MySQL via phpMyAdmin or command line
mysql -u root -p

# Create database
CREATE DATABASE myrafriend;
exit;
```

### Step 3: Import Database Schema
```bash
# Navigate to backend/database folder
cd "/mnt/d/Mobile Apps Native/MYRA/backend/database"

# Import schema
mysql -u root -p myrafriend < schema.sql

# Import master data (medications and exercises)
mysql -u root -p myrafriend < master_data.sql

# Import test users
mysql -u root -p myrafriend < test_users.sql
```

### Step 4: Configure Database Connection
Edit `backend/config/database.php`:
```php
private $host = "localhost";
private $db_name = "myrafriend";
private $username = "root";      // Your MySQL username
private $password = "";           // Your MySQL password
```

### Step 5: Set Up Backend Files
```bash
# Copy backend folder to XAMPP htdocs
cp -r "backend/" "C:/xampp/htdocs/myrafriend/"

# Or create symbolic link
ln -s "/mnt/d/Mobile Apps Native/MYRA/backend" "C:/xampp/htdocs/myrafriend"

# Set permissions (Linux/Mac)
chmod 777 backend/uploads/
chmod 777 backend/logs/
```

### Step 6: Test Backend
Open browser and visit:
```
http://localhost/myrafriend/
```

You should see: `{"success":false,"message":"Invalid endpoint"}`

Test login endpoint:
```bash
curl -X POST http://localhost/myrafriend/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"patient1@test.com","password":"Test@123"}'
```

✅ **Backend is ready if you get a JWT token response!**

---

## 📱 Android Setup (5 Minutes)

### Step 1: Open Project
```bash
# Open Android Studio
# Select "Open an Existing Project"
# Navigate to: /mnt/d/Mobile Apps Native/MYRA/android
# Click "OK"
```

### Step 2: Configure Firebase (Required for Notifications)
```bash
# 1. Go to https://console.firebase.google.com
# 2. Create new project or use existing
# 3. Add Android app with package name: com.myrafriend
# 4. Download google-services.json
# 5. Place in: android/app/google-services.json
```

### Step 3: Configure API Base URL

Edit `android/app/src/main/java/com/myrafriend/network/RetrofitClient.java`:

**For Android Emulator:**
```java
private static final String BASE_URL = "http://10.0.2.2/myrafriend/";
```

**For Physical Device on Same Network:**
```java
// Find your computer's IP (ipconfig on Windows, ifconfig on Linux/Mac)
private static final String BASE_URL = "http://192.168.1.XXX/myrafriend/";
```

### Step 4: Sync Gradle
```bash
# Android Studio will prompt to sync Gradle
# Click "Sync Now"
# Wait for dependencies to download (2-5 minutes)
```

### Step 5: Build & Run
```bash
# Click green "Run" button or press Shift + F10
# Select emulator or connected device
# App will build and launch
```

✅ **Android app is ready when login screen appears!**

---

## 🧪 Test Credentials

### Patient Account
- **Email**: patient1@test.com
- **Password**: Test@123

### Doctor Account
- **Email**: dr.smith@myrafriend.com
- **Password**: Test@123

### Admin Account
- **Email**: admin@myrafriend.com
- **Password**: Test@123

---

## ✅ Verification Checklist

### Backend
- [ ] MySQL database `myrafriend` exists
- [ ] Database has 15 tables
- [ ] Test users exist (check with: `SELECT * FROM users;`)
- [ ] Backend URL works: http://localhost/myrafriend/
- [ ] Login endpoint returns JWT token

### Android
- [ ] Project syncs without errors
- [ ] google-services.json exists in app/ folder
- [ ] API base URL configured correctly
- [ ] App builds successfully
- [ ] Login screen loads
- [ ] Can login with test credentials
- [ ] Bottom navigation appears after login

---

## 🔧 Troubleshooting

### Backend Issues

**"Database connection failed"**
- Check MySQL is running in XAMPP
- Verify database credentials in `backend/config/database.php`
- Ensure database `myrafriend` exists

**"404 Not Found"**
- Verify backend files are in `htdocs/myrafriend/`
- Check Apache is running
- Try: http://localhost/myrafriend/index.php

**"Permission denied" for uploads**
- Run: `chmod 777 backend/uploads/`
- Run: `chmod 777 backend/logs/`

### Android Issues

**"Failed to resolve dependencies"**
- Check internet connection
- Click "File → Invalidate Caches → Restart"
- Sync Gradle again

**"Cannot connect to backend"**
- For emulator: Use `http://10.0.2.2/myrafriend/`
- For device: Use your computer's local IP
- Ensure device is on same WiFi network
- Check firewall allows connections

**"google-services.json missing"**
- Download from Firebase Console
- Place in `android/app/` folder (not `android/`)
- Sync Gradle after adding

**"App crashes on startup"**
- Check Logcat for errors
- Verify AndroidManifest.xml has all permissions
- Ensure BASE_URL in RetrofitClient is correct

---

## 📖 What Works After Setup

### ✅ Fully Functional Features
1. **Login/Logout** - All three roles (Patient, Doctor, Admin)
2. **Patient Dashboard** - Quick action cards
3. **Symptom Logging** - Complete form with sliders
4. **Flare Reporting** - Severity dialog
5. **Medication List** - Data loads from API
6. **Navigation** - Bottom nav switches fragments
7. **Offline Mode** - Symptoms cached locally
8. **Push Notifications** - FCM notifications work

### ⚠️ Features with Basic UI
1. **Medication Tracking** - List displays but needs adapter
2. **Rehab Exercises** - Placeholder screen
3. **Messaging** - Placeholder screen
4. **Doctor Screens** - Basic layout, API works

---

## 🎯 Testing Flow

1. **Start Backend & Android Studio**
2. **Build and Run App**
3. **Login** as patient1@test.com
4. **Navigate** through bottom tabs
5. **Log Symptoms** - Fill form and submit
6. **Report Flare** - Click flare button
7. **Check Medications** - View assigned meds
8. **Logout and Login** as doctor
9. **Verify API** calls work in Logcat

---

## 📞 Support

If you encounter issues:
1. Check IMPLEMENTATION_STATUS.md for known limitations
2. Review Logcat in Android Studio for errors
3. Test API endpoints with Postman/curl
4. Verify database has test data

---

**Build Time**: ~10 minutes
**Status**: ✅ App runs without breaking
**Last Updated**: 2025-01-18
