# My RA Friend - Backend API

Complete PHP RESTful API for the My RA Friend rheumatoid arthritis management platform.

## Features Implemented

✅ **Authentication**
- JWT token-based authentication
- Account lockout after failed attempts
- FCM token management
- Password hashing with bcrypt

✅ **Patient Features**
- Symptom logging (VAS pain, joint count, FACIT fatigue)
- Flare reporting with doctor notifications
- Medication adherence tracking
- Rehabilitation exercise tracking
- Lab report uploads
- Secure messaging
- Appointment booking

✅ **Doctor Features**
- Patient list and detail views
- Symptom trend analysis
- Medication prescription
- Rehab exercise assignment
- Lab report interpretation
- Patient messaging

✅ **Notifications**
- FCM push notifications
- Flare alerts
- Missed medication alerts
- New message notifications
- Lab report updates

## Installation

### 1. Database Setup

```bash
# Import database schema
mysql -u root -p < database/schema.sql

# Insert master data
mysql -u root -p < database/master_data.sql

# Create test users
mysql -u root -p < database/test_users.sql
```

### 2. Configuration

Edit `config/database.php`:
```php
private $host = "localhost";
private $db_name = "my_ra_friend";
private $username = "root";
private $password = "your_password";
```

Edit `config/constants.php` - Add your FCM Server Key:
```php
define('FCM_SERVER_KEY', 'your_actual_fcm_server_key');
```

### 3. File Permissions

```bash
chmod 777 uploads/
chmod 777 logs/
```

### 4. Web Server Configuration

**Apache (.htaccess):**
```apache
RewriteEngine On
RewriteCond %{REQUEST_FILENAME} !-f
RewriteCond %{REQUEST_FILENAME} !-d
RewriteRule ^(.*)$ index.php [QSA,L]
```

**Nginx:**
```nginx
location / {
    try_files $uri $uri/ /index.php?$query_string;
}
```

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/login` | User login |
| POST | `/auth/refresh` | Refresh JWT token |
| POST | `/auth/update-fcm-token` | Update FCM token |

### Patient Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/patient/symptom-logs` | Submit symptom log |
| GET | `/patient/symptom-logs/{id}` | Get symptom history |
| POST | `/patient/flare-report` | Submit flare report |
| GET | `/patient/medications/{id}` | Get assigned medications |
| POST | `/patient/medication-intake` | Record medication intake |
| GET | `/patient/adherence-metrics/{id}` | Get adherence metrics |
| GET | `/patient/rehab/{id}` | Get assigned rehab exercises |
| POST | `/patient/rehab-completion` | Mark rehab complete |
| POST | `/patient/lab-reports` | Upload lab report |
| GET | `/patient/lab-reports/{id}` | Get lab reports |

### Doctor Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/doctor/patients/{doctor_id}` | Get assigned patients |
| GET | `/doctor/patient-detail/{patient_id}` | Get patient detail |
| POST | `/doctor/prescribe-medication` | Prescribe medication |
| POST | `/doctor/assign-rehab` | Assign rehab exercise |
| POST | `/doctor/lab-report-interpretation` | Add lab interpretation |
| GET | `/doctor/medications-master` | Get medications list |
| GET | `/doctor/rehab-exercises-master` | Get exercises list |

### Messaging & Appointments

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/messages?recipient_id={id}` | Get messages |
| POST | `/messages/send` | Send message |
| PUT | `/messages/mark-read` | Mark messages as read |
| GET | `/appointments` | Get appointments |
| POST | `/appointments/book` | Book appointment |
| PUT | `/appointments/cancel/{id}` | Cancel appointment |

## Test Credentials

**Admin:**
- Email: `admin@myrafriend.com`
- Password: `Test@123`

**Doctor:**
- Email: `dr.smith@myrafriend.com`
- Password: `Test@123`

**Patient:**
- Email: `patient1@test.com`
- Password: `Test@123`

## Request Examples

**Login:**
```bash
curl -X POST http://localhost/backend/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"patient1@test.com","password":"Test@123"}'
```

**Submit Symptom Log:**
```bash
curl -X POST http://localhost/backend/patient/symptom-logs \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "pain_level": 7,
    "joint_count": 12,
    "morning_stiffness": "30_minutes",
    "fatigue_level": 6,
    "swelling": true,
    "warmth": false,
    "functional_ability": "moderate",
    "notes": "Pain increased after activity"
  }'
```

## Error Codes

- `200` - Success
- `201` - Created
- `400` - Bad Request (validation error)
- `401` - Unauthorized (invalid/missing token)
- `403` - Forbidden (insufficient permissions)
- `404` - Not Found
- `423` - Locked (account locked)
- `500` - Internal Server Error

## Security Features

- JWT token authentication with 24-hour expiration
- Account lockout after 3 failed login attempts (15 minutes)
- Password hashing using bcrypt
- SQL injection prevention via prepared statements
- XSS prevention via input sanitization
- File upload validation (type and size)
- Role-based access control

## Development

**Enable error display:**
```php
// In index.php
ini_set('display_errors', 1);
```

**Check logs:**
```bash
tail -f logs/php_errors.log
tail -f logs/api.log
```

## Production Deployment

1. Set production database credentials
2. Change JWT secret key
3. Add actual FCM server key
4. Disable error display
5. Enable HTTPS
6. Set up automated backups
7. Configure firewall rules

## License

Proprietary - My RA Friend Platform
