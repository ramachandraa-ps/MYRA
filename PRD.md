Product Requirements Document: My RA Friend________________________________________
1. Introduction
1.1 Overview
This Product Requirements Document (PRD) defines the complete functional, technical, and design requirements for My RA Friend, a modern digital health platform created for the follow-up, rehabilitation, and long-term management of patients with Rheumatoid Arthritis (RA).
The purpose of My RA Friend is to serve as an intelligent, always-accessible companion app for both patients and clinicians, enabling structured symptom tracking, doctor-supervised medication regimens, guided rehabilitation exercises, remote monitoring, and early detection of complications—ultimately improving disease outcomes and quality of life.
The platform is clinically oriented, focusing on medical accuracy, evidence-based assessments (VAS, DAS28, FACIT), secure communication, and smooth workflows for both patients and doctors. This MVP release will emphasize accurate data collection, reliable follow-up, and seamless interaction between doctors and patients, supported by a stable, scalable backend.
________________________________________
1.2 Problem Statement
Rheumatoid Arthritis is a chronic autoimmune disease requiring continuous disease activity monitoring, regular treatment adjustments, and strict adherence to medications and exercises. Traditional care models rely heavily on periodic clinic visits, during which clinicians often face:
Patient-Side Problems
•	Patients forget symptom details due to recall bias during clinic visits.
•	Many struggle with medication adherence, especially with complex RA regimens.
•	Home exercises prescribed during appointments are rarely tracked or performed correctly.
•	Delayed follow-up results in missed flares or complications.
•	Mobility limitations, pain, or distance prevent frequent hospital visits.
Clinician-Side Problems
•	Lack of real-time symptom data makes accurate treatment adjustment difficult.
•	No reliable way to check if a patient is taking medications on time.
•	No structured data on fatigue, stiffness, or functional limitations between visits.
•	Doctors cannot assess patient adherence to prescribed rehabilitation exercises.
•	Lab reports brought during visits may be incomplete or inconsistent.
These gaps directly affect disease outcomes, as RA demands continuous monitoring, timely medical decisions, and consistent rehabilitation routines.
There is a need for a clinically guided digital platform that provides structured symptom tracking, medication management, rehab monitoring, and seamless doctor-patient communication to improve long-term RA care.
________________________________________
1.3 Goal
The primary goal of the My RA Friend MVP is to create a clinically meaningful, data-driven platform that:
•	Helps patients accurately record day-to-day symptoms like pain, stiffness, joint involvement, and fatigue.
•	Ensures that only doctor-prescribed medications and rehab exercises appear in the patient’s feed.
•	Allows doctors to remotely track disease progression, assess adherence, and identify early warning signs of complications.
•	Provides a structured comparison between app-based follow-up vs traditional follow-up, evaluating differences in:
o	flare frequency
o	medication adherence
o	rehab participation
o	complications or hospitalizations
Success is defined as delivering a stable, medically accurate, user-friendly platform with seamless onboarding, real-time notifications, accurate data capture, and reliable doctor-patient workflows that can be used in research and clinical settings.
________________________________________
2. Technical Specifications
The system will be built using a secure, scalable, and medically compliant tech stack focused on performance, reliability, and secure handling of clinical data.
________________________________________
● Mobile Application (Android)
Platform: Android (Native)
Language: Java only (strict requirement)
UI Framework: XML Layouts
Architecture: MVVM
Mobile App Components
•	View Layer: XML screens designed with a clean medical layout
•	ViewModel Layer: Manages UI state, business logic
•	Repository Layer: Handles network calls & caching
•	Local Database: Room Database to store offline symptom logs & assigned tasks
•	Networking: Retrofit for secure REST API calls
•	Notifications: FCM (Firebase Cloud Messaging)
•	Minimum Version: Android 7.0+ (API 24)
________________________________________
● Backend API
Two backend options (developer can choose either):
Option A: PHP (Preferred for Hosting Ease)
•	REST APIs built using PHP (Core PHP or Laravel)
•	JWT authentication
•	MySQL integration
•	File upload endpoints for lab reports
•	Medically structured APIs
Option B: Python Flask
•	REST Framework with Blueprints
•	JWT authentication
•	Strong logging and clinical data processing
•	Scalable and lightweight
________________________________________
Database
Type: MySQL / MariaDB
Includes tables for:
•	Users (patients, doctors, admin)
•	Doctor details
•	Patient profiles
•	Medications master
•	Doctor-assigned medications
•	Rehab exercises master
•	Doctor-assigned rehab
•	Lab reports
•	Symptom logs
•	Notifications
•	Messages
•	Appointments
•	Adherence metrics
•	Activity logs
Data integrity, foreign keys, and secure indexed structure must be enforced.
________________________________________
Authentication
•	JWT authentication
•	Password hashing using bcrypt
•	Optional: Two-factor authentication for doctors & admin
•	Session tracking and token refresh mechanisms
________________________________________
Hosting
Backend will be hosted on a secure environment supporting PHP/Flask:
•	AWS EC2
•	DigitalOcean
•	Render
•	Railway
•	cPanel/PHP hosting
Must support:
•	HTTPS
•	File uploads
•	Database connectivity
•	Long-running server processes
________________________________________
Third-Party Integrations
•	Google FCM for real-time patient/doctor notifications
•	Google Places API (optional) for easy address selection
•	Cloud/local storage for lab report uploads
•	Analytics & monitoring through backend logs
________________________________________
3. Core Features & Functionality (MVP)
The MVP consists of clinically essential features to support real-world rheumatology workflows for patients and doctors.
________________________________________
3.1 User Roles
1. Patient
Patients will use the app to:
•	Log RA symptoms with medically structured entries
•	View doctor-assigned medications only
•	Record daily dosage intake
•	Access doctor-assigned rehab sessions
•	Upload lab reports (CBC, ESR, CRP, Imaging)
•	Message doctor securely
•	View appointments and receive reminders
2. Doctor
Doctors will:
•	View patients assigned to them
•	Monitor symptoms trends over time
•	Prescribe/modify medications for each patient
•	Assign isometric rehab exercises with instructions
•	Track adherence across medications & rehab
•	Communicate with patients securely
•	Comment on lab reports and provide guidance
3. Admin
Admins are responsible for:
•	Creating and managing accounts (patient/doctor/admin)
•	System maintenance and audit logging
•	Managing educational content
•	Resetting passwords
•	Monitoring app-wide activity and performance
________________________________________
3.2 Key Feature Set (Detailed)
1. Symptom Tracking (Highly Detailed Inputs)
Patients can log medically structured symptoms. The symptom entry screen includes:
Pain Assessment (VAS)
•	Slider from 0 (no pain) to 10 (worst possible pain)
•	UI: Large slider with color mapping
•	Validation: Mandatory field
•	Use: Doctor sees daily pain trend graph
Number of Joints Involved
•	Number selector (0 to 28)
•	Includes interactive joint map (optional)
•	Helps compute DAS28
Morning Stiffness
•	Options:
o	< 30 minutes
o	30 minutes
o	“Severe stiffness lasting hours”
•	Additional notes input
Fatigue (FACIT Scale)
•	0–10 scale
•	UI: Horizontal slider
•	Used to gauge systemic inflammation
Swelling / Warmth
•	Toggle switches:
o	Swelling: Yes/No
o	Warmth: Yes/No
•	Optional text notes
Functional Ability
•	Daily living tasks difficulty scale
•	Options: No difficulty → Severe difficulty
Flare Report
•	Quick action button
•	Additional prompts:
o	“New joint pain?”
o	“New swelling?”
o	“Infection symptoms?”
•	Auto-notifies doctor
Notes
•	Free-text field for subjective symptoms
•	Allows describing triggers, stress, diet, etc.
Doctor Experience:
All logs appear in a timeline view + aggregated graphs (pain trend, stiffness trend, fatigue trend).
________________________________________
2. Doctor-Assigned Medication Module (Detailed)
Doctors create individualized medication plans.
Medication Fields Include:
•	Medication Name (from master list)
•	Category (DMARD, Steroid, Supplement, PPI, Biologic, Other)
•	Dose (e.g., 10 mg/week)
•	Frequency (Once daily, twice daily, weekly)
•	Timing (Morning/Evening/After food)
•	Duration (e.g., 4 weeks)
•	Notes (e.g., avoid alcohol with MTX)
Patient View:
•	Clean, card-based display
•	“Mark as Taken” checkbox
•	If skipped → patient must specify reason (“Forgot”, “Side effects”, “Ran out of medicine”)
Doctor Notifications:
•	If patient misses 2+ consecutive doses
•	If patient reports side effects
•	End of each week → adherence summary
________________________________________
3. Doctor-Assigned Rehab Module (Isometric Exercises)
Doctors select exercises from a master list, such as:
•	Quadriceps isometrics
•	Finger squeezing
•	Wrist flexor/Extensor isometrics
•	Shoulder isometrics
•	Neck isometrics
•	Grip strengthening
Doctor Can Configure:
•	Daily/weekly goals
•	Duration for each exercise
•	Sets & repetitions
•	Upload video or attach YouTube link
•	Add caution notes
Patient View Includes:
•	Video thumbnail or image demonstration
•	Step-by-step instructions
•	Timer for duration-based exercises
•	Completion marking
•	Weekly progress score
Doctor View Includes:
•	Adherence graphs
•	Missed exercise alerts
________________________________________
4. Lab Report Upload & Interpretation
Patients upload lab reports:
•	CBC
•	ESR
•	CRP
•	LFT, KFT (for MTX monitoring)
•	X-ray/MRI films (image upload)
Doctor can:
•	View uploaded files
•	Add written interpretation
•	Request repeat tests
Patient receives a notification when doctor comments.
________________________________________
5. Secure Messaging System
Text-based chat with:
•	Real-time notifications
•	Attachment support (images/reports)
•	Doctor-patient communication history
Messages must be stored securely with audit logs.
________________________________________
6. Appointment Scheduling
Patients:
•	Book/reschedule/cancel appointments
•	Receive reminders 24 hours and 1 hour before
Doctors:
•	Set available time slots
•	Approve or modify booking requests
________________________________________
3.3 Supported Medical Modules
The app supports essential RA-specific modules:
•	Pain monitoring (VAS)
•	DAS28 Inputs:
o	Swollen joint count
o	Tender joint count
o	CRP (manually entered or auto-read from labs)
•	Fatigue Tracking (FACIT)
•	Morning stiffness duration
•	Joint involvement mapping
•	Rehab completion scoring
•	Medication adherence monitoring
•	Doctor feedback loops
•	Complication alerts (flares, infections)
These features allow long-term tracking and clinical decision-making.
________________________________________
4. High-Level Product Wireframe & Design System
(Proposal – subject to refinement during development)
Core Design System
Color Palette
•	Primary Background: White (#FFFFFF) for a clean medical look
•	Primary Accent: Teal (#008C95) for medical trust
•	Secondary Blue: (#5EA8DC) for UI highlights
•	Text Dark: (#2F2F2F)
•	Border Gray: (#E6E6E6)
•	Error Red: (#D9534F)
•	Success Green: (#4CAF50)
________________________________________
Typography
Use a modern, readable, clinician-friendly typeface:
•	Inter or Poppins
•	Large headings, clear spacing, high contrast
________________________________________
Iconography
Simple Material Icons:
•	Joint icons
•	Pill icons
•	Exercise icons
•	Upload icons
•	Calendar
________________________________________
General UI Principles
•	Clean, minimal white design
•	No clutter or decorative elements
•	Large, readable medical text
•	Simple navigation
•	Focus on usability for patients with hand/joint pain
•	Avoid bright colors or heavy animations

