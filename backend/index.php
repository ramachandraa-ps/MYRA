<?php
/**
 * My RA Friend API Entry Point
 * RESTful API Router
 */

// Enable error reporting for development
error_reporting(E_ALL);
ini_set('display_errors', 0);
ini_set('log_errors', 1);
ini_set('error_log', __DIR__ . '/logs/php_errors.log');

// CORS headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Handle preflight requests
if ($_SERVER['REQUEST_METHOD'] == 'OPTIONS') {
    http_response_code(200);
    exit();
}

// Load configuration and controllers
require_once __DIR__ . '/config/constants.php';
require_once __DIR__ . '/middleware/AuthMiddleware.php';
require_once __DIR__ . '/middleware/RoleMiddleware.php';
require_once __DIR__ . '/controllers/AuthController.php';
require_once __DIR__ . '/controllers/PatientController.php';
require_once __DIR__ . '/controllers/DoctorController.php';
require_once __DIR__ . '/controllers/MessageController.php';

// Get request method and URI
$method = $_SERVER['REQUEST_METHOD'];
$uri = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
$uri = trim($uri, '/');

// Remove 'backend' from URI if present (for different hosting setups)
$uri = preg_replace('#^backend/?#', '', $uri);

// Split URI into segments
$segments = explode('/', $uri);

// Route the request
try {
    // ============================================
    // PUBLIC ENDPOINTS (No Authentication)
    // ============================================

    if ($segments[0] === 'auth' && $segments[1] === 'login' && $method === 'POST') {
        $controller = new AuthController();
        $controller->login();
        exit();
    }

    if ($segments[0] === 'auth' && $segments[1] === 'refresh' && $method === 'POST') {
        $controller = new AuthController();
        $controller->refreshToken();
        exit();
    }

    // ============================================
    // AUTHENTICATED ENDPOINTS
    // ============================================

    $userData = AuthMiddleware::authenticate();

    // Auth - Update FCM Token
    if ($segments[0] === 'auth' && $segments[1] === 'update-fcm-token' && $method === 'POST') {
        $controller = new AuthController();
        $controller->updateFcmToken($userData);
        exit();
    }

    // ============================================
    // PATIENT ENDPOINTS
    // ============================================

    if ($segments[0] === 'patient' && $segments[1] === 'symptom-logs' && $method === 'POST') {
        RoleMiddleware::checkRole($userData, 'patient');
        $controller = new PatientController();
        $controller->submitSymptomLog($userData);
        exit();
    }

    if ($segments[0] === 'patient' && $segments[1] === 'symptom-logs' && isset($segments[2]) && $method === 'GET') {
        $patientId = intval($segments[2]);
        $controller = new PatientController();
        $controller->getSymptomHistory($userData, $patientId);
        exit();
    }

    if ($segments[0] === 'patient' && $segments[1] === 'flare-report' && $method === 'POST') {
        RoleMiddleware::checkRole($userData, 'patient');
        $controller = new PatientController();
        $controller->submitFlareReport($userData);
        exit();
    }

    if ($segments[0] === 'patient' && $segments[1] === 'medications' && isset($segments[2]) && $method === 'GET') {
        $patientId = intval($segments[2]);
        $controller = new PatientController();
        $controller->getAssignedMedications($userData, $patientId);
        exit();
    }

    if ($segments[0] === 'patient' && $segments[1] === 'medication-intake' && $method === 'POST') {
        RoleMiddleware::checkRole($userData, 'patient');
        $controller = new PatientController();
        $controller->recordMedicationIntake($userData);
        exit();
    }

    if ($segments[0] === 'patient' && $segments[1] === 'adherence-metrics' && isset($segments[2]) && $method === 'GET') {
        $patientId = intval($segments[2]);
        $controller = new PatientController();
        $controller->getAdherenceMetrics($userData, $patientId);
        exit();
    }

    if ($segments[0] === 'patient' && $segments[1] === 'rehab' && isset($segments[2]) && $method === 'GET') {
        $patientId = intval($segments[2]);
        $controller = new PatientController();
        $controller->getAssignedRehab($userData, $patientId);
        exit();
    }

    if ($segments[0] === 'patient' && $segments[1] === 'rehab-completion' && $method === 'POST') {
        RoleMiddleware::checkRole($userData, 'patient');
        $controller = new PatientController();
        $controller->markRehabComplete($userData);
        exit();
    }

    if ($segments[0] === 'patient' && $segments[1] === 'lab-reports' && $method === 'POST') {
        RoleMiddleware::checkRole($userData, 'patient');
        $controller = new PatientController();
        $controller->uploadLabReport($userData);
        exit();
    }

    if ($segments[0] === 'patient' && $segments[1] === 'lab-reports' && isset($segments[2]) && $method === 'GET') {
        $patientId = intval($segments[2]);
        $controller = new PatientController();
        $controller->getLabReports($userData, $patientId);
        exit();
    }

    // ============================================
    // DOCTOR ENDPOINTS
    // ============================================

    if ($segments[0] === 'doctor' && $segments[1] === 'patients' && isset($segments[2]) && $method === 'GET') {
        RoleMiddleware::checkRole($userData, ['doctor', 'admin']);
        $doctorId = intval($segments[2]);
        $controller = new DoctorController();
        $controller->getAssignedPatients($userData, $doctorId);
        exit();
    }

    if ($segments[0] === 'doctor' && $segments[1] === 'patient-detail' && isset($segments[2]) && $method === 'GET') {
        RoleMiddleware::checkRole($userData, ['doctor', 'admin']);
        $patientId = intval($segments[2]);
        $controller = new DoctorController();
        $controller->getPatientDetail($userData, $patientId);
        exit();
    }

    if ($segments[0] === 'doctor' && $segments[1] === 'prescribe-medication' && $method === 'POST') {
        RoleMiddleware::checkRole($userData, 'doctor');
        $controller = new DoctorController();
        $controller->prescribeMedication($userData);
        exit();
    }

    if ($segments[0] === 'doctor' && $segments[1] === 'assign-rehab' && $method === 'POST') {
        RoleMiddleware::checkRole($userData, 'doctor');
        $controller = new DoctorController();
        $controller->assignRehab($userData);
        exit();
    }

    if ($segments[0] === 'doctor' && $segments[1] === 'lab-report-interpretation' && $method === 'POST') {
        RoleMiddleware::checkRole($userData, 'doctor');
        $controller = new DoctorController();
        $controller->addLabReportInterpretation($userData);
        exit();
    }

    if ($segments[0] === 'doctor' && $segments[1] === 'medications-master' && $method === 'GET') {
        RoleMiddleware::checkRole($userData, 'doctor');
        $controller = new DoctorController();
        $controller->getMedicationsMaster();
        exit();
    }

    if ($segments[0] === 'doctor' && $segments[1] === 'rehab-exercises-master' && $method === 'GET') {
        RoleMiddleware::checkRole($userData, 'doctor');
        $controller = new DoctorController();
        $controller->getRehabExercisesMaster();
        exit();
    }

    // ============================================
    // MESSAGING ENDPOINTS
    // ============================================

    if ($segments[0] === 'messages' && $method === 'GET') {
        $recipientId = isset($_GET['recipient_id']) ? intval($_GET['recipient_id']) : null;
        $controller = new MessageController();
        $controller->getMessages($userData, $recipientId);
        exit();
    }

    if ($segments[0] === 'messages' && $segments[1] === 'send' && $method === 'POST') {
        $controller = new MessageController();
        $controller->sendMessage($userData);
        exit();
    }

    if ($segments[0] === 'messages' && $segments[1] === 'mark-read' && $method === 'PUT') {
        $controller = new MessageController();
        $controller->markMessagesAsRead($userData);
        exit();
    }

    // ============================================
    // APPOINTMENT ENDPOINTS
    // ============================================

    if ($segments[0] === 'appointments' && $method === 'GET') {
        $controller = new MessageController();
        $controller->getAppointments($userData);
        exit();
    }

    if ($segments[0] === 'appointments' && $segments[1] === 'book' && $method === 'POST') {
        RoleMiddleware::checkRole($userData, 'patient');
        $controller = new MessageController();
        $controller->bookAppointment($userData);
        exit();
    }

    if ($segments[0] === 'appointments' && $segments[1] === 'cancel' && isset($segments[2]) && $method === 'PUT') {
        $appointmentId = intval($segments[2]);
        $controller = new MessageController();
        $controller->cancelAppointment($userData, $appointmentId);
        exit();
    }

    // ============================================
    // ROUTE NOT FOUND
    // ============================================

    http_response_code(404);
    echo json_encode(array(
        "success" => false,
        "message" => "Endpoint not found: " . $uri
    ));

} catch (Exception $e) {
    error_log("API Error: " . $e->getMessage());
    http_response_code(500);
    echo json_encode(array(
        "success" => false,
        "message" => "Internal server error"
    ));
}
