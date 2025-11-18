-- My RA Friend Test Users
-- Create test accounts for development and testing

USE my_ra_friend;

-- ============================================
-- TEST USER ACCOUNTS
-- ============================================
-- All test accounts use password: "Test@123"
-- Hashed with bcrypt cost 10

-- Admin User
INSERT INTO users (email, password, role, full_name, phone, is_active) VALUES
('admin@myrafriend.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin', 'System Administrator', '+1234567890', TRUE);

-- Doctor Users
INSERT INTO users (email, password, role, full_name, phone, is_active) VALUES
('dr.smith@myrafriend.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'doctor', 'Dr. John Smith', '+1234567891', TRUE),
('dr.johnson@myrafriend.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'doctor', 'Dr. Emily Johnson', '+1234567892', TRUE);

-- Patient Users
INSERT INTO users (email, password, role, full_name, phone, is_active) VALUES
('patient1@test.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'patient', 'Alice Williams', '+1234567893', TRUE),
('patient2@test.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'patient', 'Bob Martinez', '+1234567894', TRUE),
('patient3@test.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'patient', 'Carol Davis', '+1234567895', TRUE);

-- ============================================
-- DOCTOR PROFILES
-- ============================================

INSERT INTO doctors (user_id, specialization, license_number, hospital_affiliation, years_of_experience)
SELECT id, 'Rheumatology', 'MD-12345', 'General Hospital', 15 FROM users WHERE email = 'dr.smith@myrafriend.com';

INSERT INTO doctors (user_id, specialization, license_number, hospital_affiliation, years_of_experience)
SELECT id, 'Rheumatology', 'MD-67890', 'City Medical Center', 10 FROM users WHERE email = 'dr.johnson@myrafriend.com';

-- ============================================
-- PATIENT PROFILES
-- ============================================

INSERT INTO patients (user_id, date_of_birth, gender, address, diagnosis_date, disease_duration_years, comorbidities)
SELECT id, '1975-05-15', 'female', '123 Main St, New York, NY 10001', '2018-03-20', 7, 'Hypertension' FROM users WHERE email = 'patient1@test.com';

INSERT INTO patients (user_id, date_of_birth, gender, address, diagnosis_date, disease_duration_years, comorbidities)
SELECT id, '1982-08-22', 'male', '456 Oak Ave, Los Angeles, CA 90001', '2020-01-10', 5, 'Type 2 Diabetes' FROM users WHERE email = 'patient2@test.com';

INSERT INTO patients (user_id, date_of_birth, gender, address, diagnosis_date, disease_duration_years, comorbidities)
SELECT id, '1990-11-30', 'female', '789 Pine Rd, Chicago, IL 60601', '2021-06-15', 4, 'None' FROM users WHERE email = 'patient3@test.com';

-- ============================================
-- PATIENT-DOCTOR ASSIGNMENTS
-- ============================================

-- Assign Patient 1 and Patient 2 to Dr. Smith
INSERT INTO patient_doctor_assignments (patient_id, doctor_id, is_active)
SELECT p.id, d.id, TRUE
FROM patients p
JOIN users pu ON p.user_id = pu.id
CROSS JOIN doctors d
JOIN users du ON d.user_id = du.id
WHERE pu.email = 'patient1@test.com' AND du.email = 'dr.smith@myrafriend.com';

INSERT INTO patient_doctor_assignments (patient_id, doctor_id, is_active)
SELECT p.id, d.id, TRUE
FROM patients p
JOIN users pu ON p.user_id = pu.id
CROSS JOIN doctors d
JOIN users du ON d.user_id = du.id
WHERE pu.email = 'patient2@test.com' AND du.email = 'dr.smith@myrafriend.com';

-- Assign Patient 3 to Dr. Johnson
INSERT INTO patient_doctor_assignments (patient_id, doctor_id, is_active)
SELECT p.id, d.id, TRUE
FROM patients p
JOIN users pu ON p.user_id = pu.id
CROSS JOIN doctors d
JOIN users du ON d.user_id = du.id
WHERE pu.email = 'patient3@test.com' AND du.email = 'dr.johnson@myrafriend.com';

-- Test users created successfully
-- Login credentials:
-- Admin: admin@myrafriend.com / Test@123
-- Doctor 1: dr.smith@myrafriend.com / Test@123
-- Doctor 2: dr.johnson@myrafriend.com / Test@123
-- Patient 1: patient1@test.com / Test@123
-- Patient 2: patient2@test.com / Test@123
-- Patient 3: patient3@test.com / Test@123
