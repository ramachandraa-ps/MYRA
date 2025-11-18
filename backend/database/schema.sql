-- My RA Friend Database Schema
-- MySQL 8.0+ / MariaDB 10.5+
-- Created: 2025-01-18

CREATE DATABASE IF NOT EXISTS my_ra_friend CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE my_ra_friend;

-- ============================================
-- USER MANAGEMENT TABLES
-- ============================================

-- Users Table (Base table for all user types)
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('patient', 'doctor', 'admin') NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    fcm_token VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    failed_login_attempts INT DEFAULT 0,
    account_locked_until DATETIME NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Patients Table
CREATE TABLE patients (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    date_of_birth DATE,
    gender ENUM('male', 'female', 'other'),
    address TEXT,
    diagnosis_date DATE,
    disease_duration_years INT,
    comorbidities TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Doctors Table
CREATE TABLE doctors (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    specialization VARCHAR(255),
    license_number VARCHAR(100),
    hospital_affiliation VARCHAR(255),
    years_of_experience INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Patient-Doctor Assignments Table
CREATE TABLE patient_doctor_assignments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE,
    INDEX idx_patient_id (patient_id),
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- SYMPTOM TRACKING TABLES
-- ============================================

-- Symptom Logs Table
CREATE TABLE symptom_logs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    pain_level INT NOT NULL CHECK (pain_level >= 0 AND pain_level <= 10),
    joint_count INT NOT NULL CHECK (joint_count >= 0 AND joint_count <= 28),
    morning_stiffness ENUM('less_than_30', '30_minutes', 'severe_hours') NOT NULL,
    fatigue_level INT NOT NULL CHECK (fatigue_level >= 0 AND fatigue_level <= 10),
    swelling BOOLEAN DEFAULT FALSE,
    warmth BOOLEAN DEFAULT FALSE,
    functional_ability ENUM('no_difficulty', 'mild', 'moderate', 'severe') NOT NULL,
    notes TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    INDEX idx_patient_id (patient_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Flare Reports Table
CREATE TABLE flare_reports (
    id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    new_joint_pain BOOLEAN DEFAULT FALSE,
    new_swelling BOOLEAN DEFAULT FALSE,
    infection_symptoms BOOLEAN DEFAULT FALSE,
    notes TEXT,
    is_urgent BOOLEAN DEFAULT TRUE,
    reviewed_by_doctor BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    INDEX idx_patient_id (patient_id),
    INDEX idx_urgent (is_urgent),
    INDEX idx_reviewed (reviewed_by_doctor)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- MEDICATION MANAGEMENT TABLES
-- ============================================

-- Medications Master Table
CREATE TABLE medications (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    category ENUM('DMARD', 'Steroid', 'Supplement', 'PPI', 'Biologic', 'Other') NOT NULL,
    description TEXT,
    common_side_effects TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_category (category),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Doctor Assigned Medications Table
CREATE TABLE doctor_assigned_medications (
    id INT PRIMARY KEY AUTO_INCREMENT,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    medication_id INT NOT NULL,
    dose VARCHAR(100) NOT NULL,
    frequency ENUM('once_daily', 'twice_daily', 'thrice_daily', 'weekly', 'as_needed') NOT NULL,
    timing VARCHAR(100),
    duration VARCHAR(100),
    notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (medication_id) REFERENCES medications(id) ON DELETE CASCADE,
    INDEX idx_patient_id (patient_id),
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Medication Intake Logs Table
CREATE TABLE medication_intake_logs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    medication_assignment_id INT NOT NULL,
    patient_id INT NOT NULL,
    taken BOOLEAN NOT NULL,
    skip_reason ENUM('forgot', 'side_effects', 'ran_out', 'other'),
    skip_notes TEXT,
    taken_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (medication_assignment_id) REFERENCES doctor_assigned_medications(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    INDEX idx_patient_id (patient_id),
    INDEX idx_medication_assignment_id (medication_assignment_id),
    INDEX idx_taken_at (taken_at),
    INDEX idx_taken (taken)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- REHABILITATION TABLES
-- ============================================

-- Rehabilitation Exercises Master Table
CREATE TABLE rehab_exercises (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    target_area VARCHAR(100),
    difficulty_level ENUM('beginner', 'intermediate', 'advanced'),
    default_video_url VARCHAR(500),
    instructions TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_target_area (target_area),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Doctor Assigned Rehab Table
CREATE TABLE doctor_assigned_rehab (
    id INT PRIMARY KEY AUTO_INCREMENT,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    exercise_id INT NOT NULL,
    daily_goal INT,
    duration INT,
    sets INT,
    repetitions INT,
    video_url VARCHAR(500),
    caution_notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (exercise_id) REFERENCES rehab_exercises(id) ON DELETE CASCADE,
    INDEX idx_patient_id (patient_id),
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Rehab Completion Logs Table
CREATE TABLE rehab_completion_logs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    rehab_assignment_id INT NOT NULL,
    patient_id INT NOT NULL,
    completed BOOLEAN DEFAULT TRUE,
    duration_minutes INT,
    completed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (rehab_assignment_id) REFERENCES doctor_assigned_rehab(id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    INDEX idx_patient_id (patient_id),
    INDEX idx_rehab_assignment_id (rehab_assignment_id),
    INDEX idx_completed_at (completed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- LAB REPORTS TABLE
-- ============================================

-- Lab Reports Table
CREATE TABLE lab_reports (
    id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    report_type ENUM('CBC', 'ESR', 'CRP', 'LFT', 'KFT', 'Imaging', 'Other') NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    doctor_interpretation TEXT,
    interpreted_at DATETIME,
    uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    INDEX idx_patient_id (patient_id),
    INDEX idx_report_type (report_type),
    INDEX idx_uploaded_at (uploaded_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- COMMUNICATION TABLES
-- ============================================

-- Messages Table
CREATE TABLE messages (
    id INT PRIMARY KEY AUTO_INCREMENT,
    sender_id INT NOT NULL,
    recipient_id INT NOT NULL,
    message_content TEXT NOT NULL,
    attachment_path VARCHAR(500),
    is_read BOOLEAN DEFAULT FALSE,
    sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (recipient_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_sender_id (sender_id),
    INDEX idx_recipient_id (recipient_id),
    INDEX idx_sent_at (sent_at),
    INDEX idx_is_read (is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Appointments Table
CREATE TABLE appointments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATETIME NOT NULL,
    status ENUM('pending', 'confirmed', 'cancelled', 'completed') DEFAULT 'pending',
    notes TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE,
    INDEX idx_patient_id (patient_id),
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_appointment_date (appointment_date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Notifications Table
CREATE TABLE notifications (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    notification_type ENUM('medication_reminder', 'appointment_reminder', 'flare_alert',
                           'missed_dose_alert', 'new_message', 'lab_report_interpretation',
                           'medication_assigned', 'rehab_assigned') NOT NULL,
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    related_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_notification_type (notification_type),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- AUDIT AND LOGGING TABLES
-- ============================================

-- Activity Logs Table
CREATE TABLE activity_logs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    action VARCHAR(255) NOT NULL,
    entity_type VARCHAR(100),
    entity_id INT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at),
    INDEX idx_action (action)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Database schema created successfully
