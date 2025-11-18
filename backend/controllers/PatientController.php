<?php
/**
 * Patient Controller
 * My RA Friend - Patient-Specific Endpoints
 */

require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../utils/Validator.php';
require_once __DIR__ . '/../utils/NotificationSender.php';
require_once __DIR__ . '/../utils/FileUploader.php';

class PatientController {

    private $db;
    private $notificationSender;
    private $fileUploader;

    public function __construct() {
        $database = new Database();
        $this->db = $database->getConnection();
        $this->notificationSender = new NotificationSender();
        $this->fileUploader = new FileUploader();
    }

    /**
     * Submit symptom log
     * POST /patient/symptom-logs
     * Body: {patient_id, pain_level, joint_count, morning_stiffness, fatigue_level, swelling, warmth, functional_ability, notes}
     */
    public function submitSymptomLog($userData) {
        $data = json_decode(file_get_contents("php://input"));

        // Validate required fields
        if (!isset($data->pain_level) || !isset($data->joint_count) ||
            !isset($data->morning_stiffness) || !isset($data->fatigue_level) ||
            !isset($data->functional_ability)) {
            http_response_code(400);
            echo json_encode(array(
                "success" => false,
                "message" => "Required fields missing"
            ));
            return;
        }

        // Validate ranges
        if (!Validator::validatePainLevel($data->pain_level)) {
            http_response_code(400);
            echo json_encode(array("success" => false, "message" => "Pain level must be 0-10"));
            return;
        }

        if (!Validator::validateJointCount($data->joint_count)) {
            http_response_code(400);
            echo json_encode(array("success" => false, "message" => "Joint count must be 0-28"));
            return;
        }

        if (!Validator::validateFatigueLevel($data->fatigue_level)) {
            http_response_code(400);
            echo json_encode(array("success" => false, "message" => "Fatigue level must be 0-10"));
            return;
        }

        try {
            // Get patient_id from user_id
            $patientId = $this->getPatientIdFromUserId($userData['user_id']);

            if (!$patientId) {
                http_response_code(404);
                echo json_encode(array("success" => false, "message" => "Patient record not found"));
                return;
            }

            $query = "INSERT INTO symptom_logs
                      (patient_id, pain_level, joint_count, morning_stiffness,
                       fatigue_level, swelling, warmth, functional_ability, notes)
                      VALUES
                      (:patient_id, :pain_level, :joint_count, :morning_stiffness,
                       :fatigue_level, :swelling, :warmth, :functional_ability, :notes)";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':patient_id', $patientId);
            $stmt->bindParam(':pain_level', $data->pain_level);
            $stmt->bindParam(':joint_count', $data->joint_count);
            $stmt->bindParam(':morning_stiffness', $data->morning_stiffness);
            $stmt->bindParam(':fatigue_level', $data->fatigue_level);

            $swelling = isset($data->swelling) ? $data->swelling : false;
            $warmth = isset($data->warmth) ? $data->warmth : false;
            $notes = isset($data->notes) ? Validator::sanitizeString($data->notes) : '';

            $stmt->bindParam(':swelling', $swelling);
            $stmt->bindParam(':warmth', $warmth);
            $stmt->bindParam(':functional_ability', $data->functional_ability);
            $stmt->bindParam(':notes', $notes);

            if ($stmt->execute()) {
                http_response_code(201);
                echo json_encode(array(
                    "success" => true,
                    "message" => "Symptom log created successfully"
                ));
            } else {
                throw new Exception("Failed to create symptom log");
            }

        } catch (Exception $e) {
            error_log("Submit Symptom Log Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to create symptom log"
            ));
        }
    }

    /**
     * Get symptom history
     * GET /patient/symptom-logs/{patient_id}
     */
    public function getSymptomHistory($userData, $patientId) {
        try {
            // Verify patient owns this data
            $userPatientId = $this->getPatientIdFromUserId($userData['user_id']);

            if ($userData['role'] !== 'doctor' && $userPatientId != $patientId) {
                http_response_code(403);
                echo json_encode(array("success" => false, "message" => "Access denied"));
                return;
            }

            $query = "SELECT * FROM symptom_logs
                      WHERE patient_id = :patient_id
                      ORDER BY created_at DESC
                      LIMIT 100";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':patient_id', $patientId);
            $stmt->execute();

            $logs = $stmt->fetchAll(PDO::FETCH_ASSOC);

            http_response_code(200);
            echo json_encode(array(
                "success" => true,
                "data" => $logs
            ));

        } catch (PDOException $e) {
            error_log("Get Symptom History Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to retrieve symptom history"
            ));
        }
    }

    /**
     * Submit flare report
     * POST /patient/flare-report
     * Body: {patient_id, new_joint_pain, new_swelling, infection_symptoms, notes}
     */
    public function submitFlareReport($userData) {
        $data = json_decode(file_get_contents("php://input"));

        try {
            $patientId = $this->getPatientIdFromUserId($userData['user_id']);

            if (!$patientId) {
                http_response_code(404);
                echo json_encode(array("success" => false, "message" => "Patient record not found"));
                return;
            }

            $query = "INSERT INTO flare_reports
                      (patient_id, new_joint_pain, new_swelling, infection_symptoms, notes)
                      VALUES
                      (:patient_id, :new_joint_pain, :new_swelling, :infection_symptoms, :notes)";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':patient_id', $patientId);

            $newJointPain = isset($data->new_joint_pain) ? $data->new_joint_pain : false;
            $newSwelling = isset($data->new_swelling) ? $data->new_swelling : false;
            $infectionSymptoms = isset($data->infection_symptoms) ? $data->infection_symptoms : false;
            $notes = isset($data->notes) ? Validator::sanitizeString($data->notes) : '';

            $stmt->bindParam(':new_joint_pain', $newJointPain);
            $stmt->bindParam(':new_swelling', $newSwelling);
            $stmt->bindParam(':infection_symptoms', $infectionSymptoms);
            $stmt->bindParam(':notes', $notes);

            if ($stmt->execute()) {
                // Send notification to assigned doctor
                $doctorUserId = $this->getAssignedDoctorUserId($patientId);
                if ($doctorUserId) {
                    $this->notificationSender->sendFlareAlert($doctorUserId, $patientId);
                }

                http_response_code(201);
                echo json_encode(array(
                    "success" => true,
                    "message" => "Flare report submitted successfully. Your doctor has been notified."
                ));
            } else {
                throw new Exception("Failed to submit flare report");
            }

        } catch (Exception $e) {
            error_log("Submit Flare Report Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to submit flare report"
            ));
        }
    }

    /**
     * Get assigned medications
     * GET /patient/medications/{patient_id}
     */
    public function getAssignedMedications($userData, $patientId) {
        try {
            $userPatientId = $this->getPatientIdFromUserId($userData['user_id']);

            if ($userData['role'] !== 'doctor' && $userPatientId != $patientId) {
                http_response_code(403);
                echo json_encode(array("success" => false, "message" => "Access denied"));
                return;
            }

            $query = "SELECT dam.*, m.name as medication_name, m.category, m.common_side_effects
                      FROM doctor_assigned_medications dam
                      JOIN medications m ON dam.medication_id = m.id
                      WHERE dam.patient_id = :patient_id AND dam.is_active = 1
                      ORDER BY dam.created_at DESC";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':patient_id', $patientId);
            $stmt->execute();

            $medications = $stmt->fetchAll(PDO::FETCH_ASSOC);

            http_response_code(200);
            echo json_encode(array(
                "success" => true,
                "data" => $medications
            ));

        } catch (PDOException $e) {
            error_log("Get Assigned Medications Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to retrieve medications"
            ));
        }
    }

    /**
     * Record medication intake
     * POST /patient/medication-intake
     * Body: {medication_assignment_id, taken, skip_reason, skip_notes}
     */
    public function recordMedicationIntake($userData) {
        $data = json_decode(file_get_contents("php://input"));

        if (!isset($data->medication_assignment_id) || !isset($data->taken)) {
            http_response_code(400);
            echo json_encode(array(
                "success" => false,
                "message" => "Required fields missing"
            ));
            return;
        }

        try {
            $patientId = $this->getPatientIdFromUserId($userData['user_id']);

            $query = "INSERT INTO medication_intake_logs
                      (medication_assignment_id, patient_id, taken, skip_reason, skip_notes)
                      VALUES
                      (:medication_assignment_id, :patient_id, :taken, :skip_reason, :skip_notes)";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':medication_assignment_id', $data->medication_assignment_id);
            $stmt->bindParam(':patient_id', $patientId);
            $stmt->bindParam(':taken', $data->taken);

            $skipReason = !$data->taken && isset($data->skip_reason) ? $data->skip_reason : null;
            $skipNotes = !$data->taken && isset($data->skip_notes) ? Validator::sanitizeString($data->skip_notes) : null;

            $stmt->bindParam(':skip_reason', $skipReason);
            $stmt->bindParam(':skip_notes', $skipNotes);

            if ($stmt->execute()) {
                // Check for consecutive missed doses
                if (!$data->taken) {
                    $this->checkConsecutiveMissedDoses($patientId, $data->medication_assignment_id);
                }

                http_response_code(201);
                echo json_encode(array(
                    "success" => true,
                    "message" => "Medication intake recorded successfully"
                ));
            } else {
                throw new Exception("Failed to record medication intake");
            }

        } catch (Exception $e) {
            error_log("Record Medication Intake Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to record medication intake"
            ));
        }
    }

    /**
     * Get adherence metrics
     * GET /patient/adherence-metrics/{patient_id}
     */
    public function getAdherenceMetrics($userData, $patientId) {
        try {
            $userPatientId = $this->getPatientIdFromUserId($userData['user_id']);

            if ($userData['role'] !== 'doctor' && $userPatientId != $patientId) {
                http_response_code(403);
                echo json_encode(array("success" => false, "message" => "Access denied"));
                return;
            }

            // Medication adherence (last 7 days)
            $medQuery = "SELECT
                         COUNT(CASE WHEN taken = 1 THEN 1 END) as taken_count,
                         COUNT(*) as total_count
                         FROM medication_intake_logs
                         WHERE patient_id = :patient_id
                         AND taken_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)";

            $medStmt = $this->db->prepare($medQuery);
            $medStmt->bindParam(':patient_id', $patientId);
            $medStmt->execute();
            $medAdherence = $medStmt->fetch(PDO::FETCH_ASSOC);

            $medPercentage = $medAdherence['total_count'] > 0 ?
                round(($medAdherence['taken_count'] / $medAdherence['total_count']) * 100, 2) : 0;

            // Rehab adherence (last 7 days)
            $rehabQuery = "SELECT COUNT(*) as completed_count
                           FROM rehab_completion_logs
                           WHERE patient_id = :patient_id
                           AND completed_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)";

            $rehabStmt = $this->db->prepare($rehabQuery);
            $rehabStmt->bindParam(':patient_id', $patientId);
            $rehabStmt->execute();
            $rehabAdherence = $rehabStmt->fetch(PDO::FETCH_ASSOC);

            http_response_code(200);
            echo json_encode(array(
                "success" => true,
                "data" => array(
                    "medication_adherence" => array(
                        "taken_count" => $medAdherence['taken_count'],
                        "total_count" => $medAdherence['total_count'],
                        "percentage" => $medPercentage
                    ),
                    "rehab_completed_count" => $rehabAdherence['completed_count']
                )
            ));

        } catch (PDOException $e) {
            error_log("Get Adherence Metrics Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to retrieve adherence metrics"
            ));
        }
    }

    /**
     * Get assigned rehab exercises
     * GET /patient/rehab/{patient_id}
     */
    public function getAssignedRehab($userData, $patientId) {
        try {
            $userPatientId = $this->getPatientIdFromUserId($userData['user_id']);

            if ($userData['role'] !== 'doctor' && $userPatientId != $patientId) {
                http_response_code(403);
                echo json_encode(array("success" => false, "message" => "Access denied"));
                return;
            }

            $query = "SELECT dar.*, re.name as exercise_name, re.description,
                      re.target_area, re.difficulty_level, re.instructions
                      FROM doctor_assigned_rehab dar
                      JOIN rehab_exercises re ON dar.exercise_id = re.id
                      WHERE dar.patient_id = :patient_id AND dar.is_active = 1
                      ORDER BY dar.created_at DESC";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':patient_id', $patientId);
            $stmt->execute();

            $exercises = $stmt->fetchAll(PDO::FETCH_ASSOC);

            http_response_code(200);
            echo json_encode(array(
                "success" => true,
                "data" => $exercises
            ));

        } catch (PDOException $e) {
            error_log("Get Assigned Rehab Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to retrieve rehab exercises"
            ));
        }
    }

    /**
     * Mark rehab complete
     * POST /patient/rehab-completion
     * Body: {rehab_assignment_id, duration_minutes}
     */
    public function markRehabComplete($userData) {
        $data = json_decode(file_get_contents("php://input"));

        if (!isset($data->rehab_assignment_id)) {
            http_response_code(400);
            echo json_encode(array("success" => false, "message" => "Rehab assignment ID required"));
            return;
        }

        try {
            $patientId = $this->getPatientIdFromUserId($userData['user_id']);

            $query = "INSERT INTO rehab_completion_logs
                      (rehab_assignment_id, patient_id, completed, duration_minutes)
                      VALUES
                      (:rehab_assignment_id, :patient_id, 1, :duration_minutes)";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':rehab_assignment_id', $data->rehab_assignment_id);
            $stmt->bindParam(':patient_id', $patientId);

            $durationMinutes = isset($data->duration_minutes) ? $data->duration_minutes : null;
            $stmt->bindParam(':duration_minutes', $durationMinutes);

            if ($stmt->execute()) {
                http_response_code(201);
                echo json_encode(array(
                    "success" => true,
                    "message" => "Rehab exercise marked as complete"
                ));
            } else {
                throw new Exception("Failed to mark rehab complete");
            }

        } catch (Exception $e) {
            error_log("Mark Rehab Complete Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to mark rehab complete"
            ));
        }
    }

    /**
     * Upload lab report
     * POST /patient/lab-reports
     * Multipart: file, report_type
     */
    public function uploadLabReport($userData) {
        if (!isset($_FILES['file']) || !isset($_POST['report_type'])) {
            http_response_code(400);
            echo json_encode(array(
                "success" => false,
                "message" => "File and report type required"
            ));
            return;
        }

        try {
            $patientId = $this->getPatientIdFromUserId($userData['user_id']);

            // Upload file
            $uploadResult = $this->fileUploader->uploadLabReport($_FILES['file'], $patientId);

            if (!$uploadResult['success']) {
                http_response_code(400);
                echo json_encode($uploadResult);
                return;
            }

            // Store in database
            $query = "INSERT INTO lab_reports (patient_id, report_type, file_path)
                      VALUES (:patient_id, :report_type, :file_path)";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':patient_id', $patientId);
            $stmt->bindParam(':report_type', $_POST['report_type']);
            $stmt->bindParam(':file_path', $uploadResult['file_path']);

            if ($stmt->execute()) {
                // Notify assigned doctor
                $doctorUserId = $this->getAssignedDoctorUserId($patientId);
                if ($doctorUserId) {
                    $this->notificationSender->sendNotification(
                        $doctorUserId,
                        'lab_report_uploaded',
                        'New Lab Report',
                        'A patient has uploaded a new lab report for review.',
                        $patientId
                    );
                }

                http_response_code(201);
                echo json_encode(array(
                    "success" => true,
                    "message" => "Lab report uploaded successfully",
                    "file_path" => $uploadResult['file_path']
                ));
            } else {
                throw new Exception("Failed to save lab report");
            }

        } catch (Exception $e) {
            error_log("Upload Lab Report Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to upload lab report"
            ));
        }
    }

    /**
     * Get lab reports
     * GET /patient/lab-reports/{patient_id}
     */
    public function getLabReports($userData, $patientId) {
        try {
            $userPatientId = $this->getPatientIdFromUserId($userData['user_id']);

            if ($userData['role'] !== 'doctor' && $userPatientId != $patientId) {
                http_response_code(403);
                echo json_encode(array("success" => false, "message" => "Access denied"));
                return;
            }

            $query = "SELECT * FROM lab_reports
                      WHERE patient_id = :patient_id
                      ORDER BY uploaded_at DESC";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':patient_id', $patientId);
            $stmt->execute();

            $reports = $stmt->fetchAll(PDO::FETCH_ASSOC);

            http_response_code(200);
            echo json_encode(array(
                "success" => true,
                "data" => $reports
            ));

        } catch (PDOException $e) {
            error_log("Get Lab Reports Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to retrieve lab reports"
            ));
        }
    }

    /**
     * Helper: Get patient ID from user ID
     * @param int $userId
     * @return int|null
     */
    private function getPatientIdFromUserId($userId) {
        try {
            $query = "SELECT id FROM patients WHERE user_id = :user_id LIMIT 1";
            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':user_id', $userId);
            $stmt->execute();

            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            return $row ? $row['id'] : null;
        } catch (PDOException $e) {
            error_log("Get Patient ID Error: " . $e->getMessage());
            return null;
        }
    }

    /**
     * Helper: Get assigned doctor user ID
     * @param int $patientId
     * @return int|null
     */
    private function getAssignedDoctorUserId($patientId) {
        try {
            $query = "SELECT d.user_id FROM patient_doctor_assignments pda
                      JOIN doctors d ON pda.doctor_id = d.id
                      WHERE pda.patient_id = :patient_id AND pda.is_active = 1
                      LIMIT 1";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':patient_id', $patientId);
            $stmt->execute();

            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            return $row ? $row['user_id'] : null;
        } catch (PDOException $e) {
            return null;
        }
    }

    /**
     * Helper: Check consecutive missed doses
     * @param int $patientId
     * @param int $medicationAssignmentId
     */
    private function checkConsecutiveMissedDoses($patientId, $medicationAssignmentId) {
        try {
            $query = "SELECT COUNT(*) as missed_count
                      FROM medication_intake_logs
                      WHERE patient_id = :patient_id
                      AND medication_assignment_id = :medication_assignment_id
                      AND taken = 0
                      AND taken_at >= DATE_SUB(NOW(), INTERVAL 2 DAY)";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':patient_id', $patientId);
            $stmt->bindParam(':medication_assignment_id', $medicationAssignmentId);
            $stmt->execute();

            $result = $stmt->fetch(PDO::FETCH_ASSOC);

            if ($result['missed_count'] >= 2) {
                $doctorUserId = $this->getAssignedDoctorUserId($patientId);
                if ($doctorUserId) {
                    $this->notificationSender->sendMissedDoseAlert($doctorUserId, $patientId, $medicationAssignmentId);
                }
            }
        } catch (PDOException $e) {
            error_log("Check Consecutive Missed Doses Error: " . $e->getMessage());
        }
    }
}
