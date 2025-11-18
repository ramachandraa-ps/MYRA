<?php
/**
 * Notification Sender Utility
 * My RA Friend - FCM Push Notification Handler
 */

require_once __DIR__ . '/../config/constants.php';
require_once __DIR__ . '/../config/database.php';

class NotificationSender {

    private $db;

    public function __construct() {
        $database = new Database();
        $this->db = $database->getConnection();
    }

    /**
     * Send push notification via FCM
     * @param int $userId User ID to send notification to
     * @param string $type Notification type
     * @param string $title Notification title
     * @param string $body Notification body
     * @param int|null $relatedId Related entity ID
     * @return bool
     */
    public function sendNotification($userId, $type, $title, $body, $relatedId = null) {
        // Store notification in database
        $this->storeNotification($userId, $type, $title, $body, $relatedId);

        // Get user's FCM token
        $fcmToken = $this->getUserFcmToken($userId);

        if (empty($fcmToken)) {
            error_log("No FCM token for user ID: " . $userId);
            return false;
        }

        // Send FCM notification
        return $this->sendFcmMessage($fcmToken, $type, $title, $body, $relatedId);
    }

    /**
     * Send flare alert to doctor
     * @param int $doctorId Doctor user ID
     * @param int $patientId Patient ID
     * @return bool
     */
    public function sendFlareAlert($doctorId, $patientId) {
        $patientName = $this->getPatientName($patientId);
        return $this->sendNotification(
            $doctorId,
            'flare_alert',
            'Urgent: Patient Flare Report',
            $patientName . ' has reported a flare. Please review immediately.',
            $patientId
        );
    }

    /**
     * Send missed dose alert to doctor
     * @param int $doctorId Doctor user ID
     * @param int $patientId Patient ID
     * @param int $medicationAssignmentId Medication assignment ID
     * @return bool
     */
    public function sendMissedDoseAlert($doctorId, $patientId, $medicationAssignmentId) {
        $patientName = $this->getPatientName($patientId);
        return $this->sendNotification(
            $doctorId,
            'missed_dose_alert',
            'Patient Missed Medication',
            $patientName . ' has missed 2+ consecutive medication doses.',
            $patientId
        );
    }

    /**
     * Send medication assigned notification to patient
     * @param int $patientUserId Patient user ID
     * @return bool
     */
    public function sendMedicationAssigned($patientUserId) {
        return $this->sendNotification(
            $patientUserId,
            'medication_assigned',
            'New Medication Prescribed',
            'Your doctor has prescribed new medication. Please check your medications list.',
            null
        );
    }

    /**
     * Send rehab assigned notification to patient
     * @param int $patientUserId Patient user ID
     * @return bool
     */
    public function sendRehabAssigned($patientUserId) {
        return $this->sendNotification(
            $patientUserId,
            'rehab_assigned',
            'New Exercise Assigned',
            'Your doctor has assigned new rehabilitation exercises. Please check your rehab list.',
            null
        );
    }

    /**
     * Send new message notification
     * @param int $recipientId Recipient user ID
     * @param int $senderId Sender user ID
     * @return bool
     */
    public function sendNewMessageNotification($recipientId, $senderId) {
        $senderName = $this->getUserName($senderId);
        return $this->sendNotification(
            $recipientId,
            'new_message',
            'New Message',
            'You have a new message from ' . $senderName,
            $senderId
        );
    }

    /**
     * Send lab report interpretation notification
     * @param int $patientUserId Patient user ID
     * @param int $labReportId Lab report ID
     * @return bool
     */
    public function sendLabReportInterpretation($patientUserId, $labReportId) {
        return $this->sendNotification(
            $patientUserId,
            'lab_report_interpretation',
            'Lab Report Reviewed',
            'Your doctor has added interpretation to your lab report.',
            $labReportId
        );
    }

    /**
     * Store notification in database
     * @param int $userId
     * @param string $type
     * @param string $title
     * @param string $body
     * @param int|null $relatedId
     * @return bool
     */
    private function storeNotification($userId, $type, $title, $body, $relatedId) {
        try {
            $query = "INSERT INTO notifications (user_id, notification_type, title, body, related_id)
                      VALUES (:user_id, :notification_type, :title, :body, :related_id)";

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':user_id', $userId);
            $stmt->bindParam(':notification_type', $type);
            $stmt->bindParam(':title', $title);
            $stmt->bindParam(':body', $body);
            $stmt->bindParam(':related_id', $relatedId);

            return $stmt->execute();
        } catch (PDOException $e) {
            error_log("Store Notification Error: " . $e->getMessage());
            return false;
        }
    }

    /**
     * Get user's FCM token
     * @param int $userId
     * @return string|null
     */
    private function getUserFcmToken($userId) {
        try {
            $query = "SELECT fcm_token FROM users WHERE id = :user_id LIMIT 1";
            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':user_id', $userId);
            $stmt->execute();

            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            return $row ? $row['fcm_token'] : null;
        } catch (PDOException $e) {
            error_log("Get FCM Token Error: " . $e->getMessage());
            return null;
        }
    }

    /**
     * Send FCM message
     * @param string $fcmToken
     * @param string $type
     * @param string $title
     * @param string $body
     * @param int|null $relatedId
     * @return bool
     */
    private function sendFcmMessage($fcmToken, $type, $title, $body, $relatedId) {
        $notification = array(
            'title' => $title,
            'body' => $body,
            'sound' => 'default',
            'badge' => '1'
        );

        $data = array(
            'type' => $type,
            'related_id' => (string)$relatedId,
            'click_action' => 'FLUTTER_NOTIFICATION_CLICK'
        );

        $fields = array(
            'to' => $fcmToken,
            'notification' => $notification,
            'data' => $data,
            'priority' => 'high'
        );

        $headers = array(
            'Authorization: key=' . FCM_SERVER_KEY,
            'Content-Type: application/json'
        );

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, FCM_API_URL);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

        $result = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        curl_close($ch);

        if ($httpCode === 200) {
            return true;
        } else {
            error_log("FCM Send Error: HTTP " . $httpCode . " - " . $result);
            return false;
        }
    }

    /**
     * Get patient name
     * @param int $patientId
     * @return string
     */
    private function getPatientName($patientId) {
        try {
            $query = "SELECT u.full_name FROM patients p
                      JOIN users u ON p.user_id = u.id
                      WHERE p.id = :patient_id LIMIT 1";
            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':patient_id', $patientId);
            $stmt->execute();

            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            return $row ? $row['full_name'] : 'Patient';
        } catch (PDOException $e) {
            return 'Patient';
        }
    }

    /**
     * Get user name
     * @param int $userId
     * @return string
     */
    private function getUserName($userId) {
        try {
            $query = "SELECT full_name FROM users WHERE id = :user_id LIMIT 1";
            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':user_id', $userId);
            $stmt->execute();

            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            return $row ? $row['full_name'] : 'User';
        } catch (PDOException $e) {
            return 'User';
        }
    }
}
