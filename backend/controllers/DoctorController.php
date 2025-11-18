<?php
/**
 * Doctor Controller
 * My RA Friend - Doctor-Specific Endpoints
 */

require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../utils/Validator.php';
require_once __DIR__ . '/../utils/NotificationSender.php';

class DoctorController {

    private $db;
    private $notificationSender;

    public function __construct() {
        $database = new Database();
        $this->db = $database->getConnection();
        $this->notificationSender = new NotificationSender();
    }

    /**
     * Get assigned patients
     * GET /doctor/patients/{doctor_id}
     */
    public function getAssignedPatients($userData, $doctorId) {
        try {
            $userDoctorId = $this->getDoctorIdFromUserId($userData['user_id']);

            if ($userData['role'] !== 'admin' && $userDoctorId != $doctorId) {
                http_response_code(403);
                echo json_encode(array("success" => false, "message" => "Access denied"));
                return;
            }

            $query = "SELECT p.*, u.full_name, u.email, u.phone
                      FROM patients p
                      JOIN users u ON p.user_id = u.id
                      JOIN patient_doctor_assignments pda ON p.id = pda.patient_id
                      WHERE pda.doctor_id = :doctor_id AND pda.is_active = 1
                      ORDER BY u.full_name ASC";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':doctor_id', $doctorId);
            $stmt->execute();

            $patients = $stmt->fetchAll(PDO::FETCH_ASSOC);

            http_response_code(200);
            echo json_encode(array(
                "success" => true,
                "data" => $patients
            ));

        } catch (PDOException $e) {
            error_log("Get Assigned Patients Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to retrieve patients"
            ));
        }
    }

    /**
     * Get patient detail with symptom trends and adherence
     * GET /doctor/patient-detail/{patient_id}
     */
    public function getPatientDetail($userData, $patientId) {
        try {
            // Get patient info
            $patientQuery = "SELECT p.*, u.full_name, u.email, u.phone
                             FROM patients p
                             JOIN users u ON p.user_id = u.id
                             WHERE p.id = :patient_id";

            $stmt = $this->db->prepare($patientQuery);
            $stmt->bindParam(':patient_id', $patientId);
            $stmt->execute();
            $patient = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$patient) {
                http_response_code(404);
                echo json_encode(array("success" => false, "message" => "Patient not found"));
                return;
            }

            // Get recent symptom logs (last 30 days)
            $symptomQuery = "SELECT * FROM symptom_logs
                             WHERE patient_id = :patient_id
                             ORDER BY created_at DESC
                             LIMIT 30";

            $stmt = $this->db->prepare($symptomQuery);
            $stmt->bindParam(':patient_id', $patientId);
            $stmt->execute();
            $symptoms = $stmt->fetchAll(PDO::FETCH_ASSOC);

            // Get adherence metrics (last 7 days)
            $adherenceQuery = "SELECT
                               COUNT(CASE WHEN taken = 1 THEN 1 END) as taken_count,
                               COUNT(*) as total_count
                               FROM medication_intake_logs
                               WHERE patient_id = :patient_id
                               AND taken_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)";

            $stmt = $this->db->prepare($adherenceQuery);
            $stmt->bindParam(':patient_id', $patientId);
            $stmt->execute();
            $adherence = $stmt->fetch(PDO::FETCH_ASSOC);

            $adherencePercentage = $adherence['total_count'] > 0 ?
                round(($adherence['taken_count'] / $adherence['total_count']) * 100, 2) : 0;

            // Get flare reports
            $flareQuery = "SELECT * FROM flare_reports
                           WHERE patient_id = :patient_id
                           ORDER BY created_at DESC
                           LIMIT 10";

            $stmt = $this->db->prepare($flareQuery);
            $stmt->bindParam(':patient_id', $patientId);
            $stmt->execute();
            $flareReports = $stmt->fetchAll(PDO::FETCH_ASSOC);

            $response = array(
                "patient" => $patient,
                "symptom_logs" => $symptoms,
                "adherence_metrics" => array(
                    "taken_count" => $adherence['taken_count'],
                    "total_count" => $adherence['total_count'],
                    "percentage" => $adherencePercentage
                ),
                "flare_reports" => $flareReports
            );

            http_response_code(200);
            echo json_encode(array(
                "success" => true,
                "data" => $response
            ));

        } catch (PDOException $e) {
            error_log("Get Patient Detail Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to retrieve patient detail"
            ));
        }
    }

    /**
     * Prescribe medication
     * POST /doctor/prescribe-medication
     * Body: {patient_id, medication_id, dose, frequency, timing, duration, notes}
     */
    public function prescribeMedication($userData) {
        $data = json_decode(file_get_contents("php://input"));

        if (!isset($data->patient_id) || !isset($data->medication_id) ||
            !isset($data->dose) || !isset($data->frequency)) {
            http_response_code(400);
            echo json_encode(array(
                "success" => false,
                "message" => "Required fields missing"
            ));
            return;
        }

        try {
            $doctorId = $this->getDoctorIdFromUserId($userData['user_id']);

            $query = "INSERT INTO doctor_assigned_medications
                      (doctor_id, patient_id, medication_id, dose, frequency, timing, duration, notes)
                      VALUES
                      (:doctor_id, :patient_id, :medication_id, :dose, :frequency, :timing, :duration, :notes)";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':doctor_id', $doctorId);
            $stmt->bindParam(':patient_id', $data->patient_id);
            $stmt->bindParam(':medication_id', $data->medication_id);
            $stmt->bindParam(':dose', $data->dose);
            $stmt->bindParam(':frequency', $data->frequency);

            $timing = isset($data->timing) ? $data->timing : '';
            $duration = isset($data->duration) ? $data->duration : '';
            $notes = isset($data->notes) ? Validator::sanitizeString($data->notes) : '';

            $stmt->bindParam(':timing', $timing);
            $stmt->bindParam(':duration', $duration);
            $stmt->bindParam(':notes', $notes);

            if ($stmt->execute()) {
                // Get patient user ID and send notification
                $patientUserId = $this->getPatientUserIdFromPatientId($data->patient_id);
                if ($patientUserId) {
                    $this->notificationSender->sendMedicationAssigned($patientUserId);
                }

                http_response_code(201);
                echo json_encode(array(
                    "success" => true,
                    "message" => "Medication prescribed successfully"
                ));
            } else {
                throw new Exception("Failed to prescribe medication");
            }

        } catch (Exception $e) {
            error_log("Prescribe Medication Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to prescribe medication"
            ));
        }
    }

    /**
     * Assign rehab exercise
     * POST /doctor/assign-rehab
     * Body: {patient_id, exercise_id, daily_goal, duration, sets, repetitions, video_url, caution_notes}
     */
    public function assignRehab($userData) {
        $data = json_decode(file_get_contents("php://input"));

        if (!isset($data->patient_id) || !isset($data->exercise_id)) {
            http_response_code(400);
            echo json_encode(array(
                "success" => false,
                "message" => "Patient ID and exercise ID required"
            ));
            return;
        }

        try {
            $doctorId = $this->getDoctorIdFromUserId($userData['user_id']);

            $query = "INSERT INTO doctor_assigned_rehab
                      (doctor_id, patient_id, exercise_id, daily_goal, duration, sets, repetitions, video_url, caution_notes)
                      VALUES
                      (:doctor_id, :patient_id, :exercise_id, :daily_goal, :duration, :sets, :repetitions, :video_url, :caution_notes)";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':doctor_id', $doctorId);
            $stmt->bindParam(':patient_id', $data->patient_id);
            $stmt->bindParam(':exercise_id', $data->exercise_id);

            $dailyGoal = isset($data->daily_goal) ? $data->daily_goal : null;
            $duration = isset($data->duration) ? $data->duration : null;
            $sets = isset($data->sets) ? $data->sets : null;
            $repetitions = isset($data->repetitions) ? $data->repetitions : null;
            $videoUrl = isset($data->video_url) ? $data->video_url : null;
            $cautionNotes = isset($data->caution_notes) ? Validator::sanitizeString($data->caution_notes) : null;

            $stmt->bindParam(':daily_goal', $dailyGoal);
            $stmt->bindParam(':duration', $duration);
            $stmt->bindParam(':sets', $sets);
            $stmt->bindParam(':repetitions', $repetitions);
            $stmt->bindParam(':video_url', $videoUrl);
            $stmt->bindParam(':caution_notes', $cautionNotes);

            if ($stmt->execute()) {
                // Send notification to patient
                $patientUserId = $this->getPatientUserIdFromPatientId($data->patient_id);
                if ($patientUserId) {
                    $this->notificationSender->sendRehabAssigned($patientUserId);
                }

                http_response_code(201);
                echo json_encode(array(
                    "success" => true,
                    "message" => "Rehab exercise assigned successfully"
                ));
            } else {
                throw new Exception("Failed to assign rehab");
            }

        } catch (Exception $e) {
            error_log("Assign Rehab Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to assign rehab exercise"
            ));
        }
    }

    /**
     * Add lab report interpretation
     * POST /doctor/lab-report-interpretation
     * Body: {lab_report_id, interpretation}
     */
    public function addLabReportInterpretation($userData) {
        $data = json_decode(file_get_contents("php://input"));

        if (!isset($data->lab_report_id) || !isset($data->interpretation)) {
            http_response_code(400);
            echo json_encode(array(
                "success" => false,
                "message" => "Lab report ID and interpretation required"
            ));
            return;
        }

        try {
            $query = "UPDATE lab_reports
                      SET doctor_interpretation = :interpretation,
                          interpreted_at = NOW()
                      WHERE id = :lab_report_id";

            $stmt = $this->db->prepare($query);
            $interpretation = Validator::sanitizeString($data->interpretation);
            $stmt->bindParam(':interpretation', $interpretation);
            $stmt->bindParam(':lab_report_id', $data->lab_report_id);

            if ($stmt->execute()) {
                // Get patient user ID and notify
                $patientQuery = "SELECT p.user_id FROM lab_reports lr
                                 JOIN patients p ON lr.patient_id = p.id
                                 WHERE lr.id = :lab_report_id";
                $patientStmt = $this->db->prepare($patientQuery);
                $patientStmt->bindParam(':lab_report_id', $data->lab_report_id);
                $patientStmt->execute();
                $patient = $patientStmt->fetch(PDO::FETCH_ASSOC);

                if ($patient) {
                    $this->notificationSender->sendLabReportInterpretation($patient['user_id'], $data->lab_report_id);
                }

                http_response_code(200);
                echo json_encode(array(
                    "success" => true,
                    "message" => "Interpretation added successfully"
                ));
            } else {
                throw new Exception("Failed to add interpretation");
            }

        } catch (Exception $e) {
            error_log("Add Lab Report Interpretation Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to add interpretation"
            ));
        }
    }

    /**
     * Get medications master list
     * GET /doctor/medications-master
     */
    public function getMedicationsMaster() {
        try {
            $query = "SELECT * FROM medications ORDER BY category, name";
            $stmt = $this->db->query($query);
            $medications = $stmt->fetchAll(PDO::FETCH_ASSOC);

            http_response_code(200);
            echo json_encode(array(
                "success" => true,
                "data" => $medications
            ));

        } catch (PDOException $e) {
            error_log("Get Medications Master Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to retrieve medications"
            ));
        }
    }

    /**
     * Get rehab exercises master list
     * GET /doctor/rehab-exercises-master
     */
    public function getRehabExercisesMaster() {
        try {
            $query = "SELECT * FROM rehab_exercises ORDER BY target_area, name";
            $stmt = $this->db->query($query);
            $exercises = $stmt->fetchAll(PDO::FETCH_ASSOC);

            http_response_code(200);
            echo json_encode(array(
                "success" => true,
                "data" => $exercises
            ));

        } catch (PDOException $e) {
            error_log("Get Rehab Exercises Master Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to retrieve exercises"
            ));
        }
    }

    /**
     * Helper: Get doctor ID from user ID
     * @param int $userId
     * @return int|null
     */
    private function getDoctorIdFromUserId($userId) {
        try {
            $query = "SELECT id FROM doctors WHERE user_id = :user_id LIMIT 1";
            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':user_id', $userId);
            $stmt->execute();

            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            return $row ? $row['id'] : null;
        } catch (PDOException $e) {
            error_log("Get Doctor ID Error: " . $e->getMessage());
            return null;
        }
    }

    /**
     * Helper: Get patient user ID from patient ID
     * @param int $patientId
     * @return int|null
     */
    private function getPatientUserIdFromPatientId($patientId) {
        try {
            $query = "SELECT user_id FROM patients WHERE id = :patient_id LIMIT 1";
            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':patient_id', $patientId);
            $stmt->execute();

            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            return $row ? $row['user_id'] : null;
        } catch (PDOException $e) {
            return null;
        }
    }
}
