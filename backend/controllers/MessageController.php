<?php
/**
 * Message Controller
 * My RA Friend - Messaging and Appointments
 */

require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../utils/Validator.php';
require_once __DIR__ . '/../utils/NotificationSender.php';
require_once __DIR__ . '/../utils/FileUploader.php';

class MessageController {

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
     * Get messages
     * GET /messages/{user_id}?recipient_id={recipient_id}
     */
    public function getMessages($userData, $recipientId = null) {
        try {
            if ($recipientId) {
                // Get conversation with specific user
                $query = "SELECT * FROM messages
                          WHERE (sender_id = :user_id AND recipient_id = :recipient_id)
                          OR (sender_id = :recipient_id AND recipient_id = :user_id)
                          ORDER BY sent_at ASC";

                $stmt = $this->db->prepare($query);
                $stmt->bindParam(':user_id', $userData['user_id']);
                $stmt->bindParam(':recipient_id', $recipientId);
            } else {
                // Get all messages for user
                $query = "SELECT * FROM messages
                          WHERE sender_id = :user_id OR recipient_id = :user_id
                          ORDER BY sent_at DESC";

                $stmt = $this->db->prepare($query);
                $stmt->bindParam(':user_id', $userData['user_id']);
            }

            $stmt->execute();
            $messages = $stmt->fetchAll(PDO::FETCH_ASSOC);

            http_response_code(200);
            echo json_encode(array(
                "success" => true,
                "data" => $messages
            ));

        } catch (PDOException $e) {
            error_log("Get Messages Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to retrieve messages"
            ));
        }
    }

    /**
     * Send message
     * POST /messages/send
     * Body: {recipient_id, message_content}
     * Optional file attachment
     */
    public function sendMessage($userData) {
        $data = json_decode(file_get_contents("php://input"));

        if (!isset($data->recipient_id) || !isset($data->message_content)) {
            http_response_code(400);
            echo json_encode(array(
                "success" => false,
                "message" => "Recipient ID and message content required"
            ));
            return;
        }

        try {
            $attachmentPath = null;

            // Handle file attachment if present
            if (isset($_FILES['attachment'])) {
                $uploadResult = $this->fileUploader->uploadMessageAttachment(
                    $_FILES['attachment'],
                    $userData['user_id']
                );

                if ($uploadResult['success']) {
                    $attachmentPath = $uploadResult['file_path'];
                }
            }

            $query = "INSERT INTO messages (sender_id, recipient_id, message_content, attachment_path)
                      VALUES (:sender_id, :recipient_id, :message_content, :attachment_path)";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':sender_id', $userData['user_id']);
            $stmt->bindParam(':recipient_id', $data->recipient_id);
            $messageContent = Validator::sanitizeString($data->message_content);
            $stmt->bindParam(':message_content', $messageContent);
            $stmt->bindParam(':attachment_path', $attachmentPath);

            if ($stmt->execute()) {
                // Send notification to recipient
                $this->notificationSender->sendNewMessageNotification(
                    $data->recipient_id,
                    $userData['user_id']
                );

                http_response_code(201);
                echo json_encode(array(
                    "success" => true,
                    "message" => "Message sent successfully"
                ));
            } else {
                throw new Exception("Failed to send message");
            }

        } catch (Exception $e) {
            error_log("Send Message Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to send message"
            ));
        }
    }

    /**
     * Mark messages as read
     * PUT /messages/mark-read
     * Body: {message_ids: []}
     */
    public function markMessagesAsRead($userData) {
        $data = json_decode(file_get_contents("php://input"));

        if (!isset($data->message_ids) || !is_array($data->message_ids)) {
            http_response_code(400);
            echo json_encode(array(
                "success" => false,
                "message" => "Message IDs array required"
            ));
            return;
        }

        try {
            $placeholders = implode(',', array_fill(0, count($data->message_ids), '?'));
            $query = "UPDATE messages SET is_read = 1
                      WHERE id IN ($placeholders) AND recipient_id = ?";

            $stmt = $this->db->prepare($query);
            $params = array_merge($data->message_ids, [$userData['user_id']]);
            $stmt->execute($params);

            http_response_code(200);
            echo json_encode(array(
                "success" => true,
                "message" => "Messages marked as read"
            ));

        } catch (PDOException $e) {
            error_log("Mark Messages Read Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to mark messages as read"
            ));
        }
    }

    /**
     * Get appointments
     * GET /appointments/{user_id}
     */
    public function getAppointments($userData) {
        try {
            $query = "SELECT a.*,
                      ps.full_name as patient_name,
                      ds.full_name as doctor_name
                      FROM appointments a
                      LEFT JOIN patients p ON a.patient_id = p.id
                      LEFT JOIN users ps ON p.user_id = ps.id
                      LEFT JOIN doctors d ON a.doctor_id = d.id
                      LEFT JOIN users ds ON d.user_id = ds.id
                      WHERE ps.id = :user_id OR ds.id = :user_id
                      ORDER BY a.appointment_date DESC";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':user_id', $userData['user_id']);
            $stmt->execute();

            $appointments = $stmt->fetchAll(PDO::FETCH_ASSOC);

            http_response_code(200);
            echo json_encode(array(
                "success" => true,
                "data" => $appointments
            ));

        } catch (PDOException $e) {
            error_log("Get Appointments Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to retrieve appointments"
            ));
        }
    }

    /**
     * Book appointment
     * POST /appointments/book
     * Body: {doctor_id, appointment_date, notes}
     */
    public function bookAppointment($userData) {
        $data = json_decode(file_get_contents("php://input"));

        if (!isset($data->doctor_id) || !isset($data->appointment_date)) {
            http_response_code(400);
            echo json_encode(array(
                "success" => false,
                "message" => "Doctor ID and appointment date required"
            ));
            return;
        }

        try {
            // Get patient ID from user ID
            $patientQuery = "SELECT id FROM patients WHERE user_id = :user_id";
            $patientStmt = $this->db->prepare($patientQuery);
            $patientStmt->bindParam(':user_id', $userData['user_id']);
            $patientStmt->execute();
            $patient = $patientStmt->fetch(PDO::FETCH_ASSOC);

            if (!$patient) {
                http_response_code(404);
                echo json_encode(array("success" => false, "message" => "Patient record not found"));
                return;
            }

            $query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, notes, status)
                      VALUES (:patient_id, :doctor_id, :appointment_date, :notes, 'pending')";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':patient_id', $patient['id']);
            $stmt->bindParam(':doctor_id', $data->doctor_id);
            $stmt->bindParam(':appointment_date', $data->appointment_date);

            $notes = isset($data->notes) ? Validator::sanitizeString($data->notes) : '';
            $stmt->bindParam(':notes', $notes);

            if ($stmt->execute()) {
                // Get doctor user ID and notify
                $doctorQuery = "SELECT user_id FROM doctors WHERE id = :doctor_id";
                $doctorStmt = $this->db->prepare($doctorQuery);
                $doctorStmt->bindParam(':doctor_id', $data->doctor_id);
                $doctorStmt->execute();
                $doctor = $doctorStmt->fetch(PDO::FETCH_ASSOC);

                if ($doctor) {
                    $this->notificationSender->sendNotification(
                        $doctor['user_id'],
                        'appointment_request',
                        'New Appointment Request',
                        'A patient has requested an appointment. Please review.',
                        $patient['id']
                    );
                }

                http_response_code(201);
                echo json_encode(array(
                    "success" => true,
                    "message" => "Appointment booked successfully"
                ));
            } else {
                throw new Exception("Failed to book appointment");
            }

        } catch (Exception $e) {
            error_log("Book Appointment Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to book appointment"
            ));
        }
    }

    /**
     * Cancel appointment
     * PUT /appointments/cancel/{appointment_id}
     */
    public function cancelAppointment($userData, $appointmentId) {
        try {
            // Verify user owns this appointment
            $query = "SELECT a.*, p.user_id as patient_user_id, d.user_id as doctor_user_id
                      FROM appointments a
                      LEFT JOIN patients p ON a.patient_id = p.id
                      LEFT JOIN doctors d ON a.doctor_id = d.id
                      WHERE a.id = :appointment_id";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':appointment_id', $appointmentId);
            $stmt->execute();
            $appointment = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$appointment) {
                http_response_code(404);
                echo json_encode(array("success" => false, "message" => "Appointment not found"));
                return;
            }

            if ($appointment['patient_user_id'] != $userData['user_id'] &&
                $appointment['doctor_user_id'] != $userData['user_id']) {
                http_response_code(403);
                echo json_encode(array("success" => false, "message" => "Access denied"));
                return;
            }

            // Update status
            $updateQuery = "UPDATE appointments SET status = 'cancelled' WHERE id = :appointment_id";
            $updateStmt = $this->db->prepare($updateQuery);
            $updateStmt->bindParam(':appointment_id', $appointmentId);

            if ($updateStmt->execute()) {
                // Notify the other party
                $recipientId = ($appointment['patient_user_id'] == $userData['user_id']) ?
                    $appointment['doctor_user_id'] : $appointment['patient_user_id'];

                $this->notificationSender->sendNotification(
                    $recipientId,
                    'appointment_cancelled',
                    'Appointment Cancelled',
                    'An appointment has been cancelled.',
                    $appointmentId
                );

                http_response_code(200);
                echo json_encode(array(
                    "success" => true,
                    "message" => "Appointment cancelled successfully"
                ));
            } else {
                throw new Exception("Failed to cancel appointment");
            }

        } catch (Exception $e) {
            error_log("Cancel Appointment Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to cancel appointment"
            ));
        }
    }
}
