<?php
/**
 * Validator Utility
 * My RA Friend - Input Validation Functions
 */

class Validator {

    /**
     * Validate email address
     * @param string $email
     * @return bool
     */
    public static function validateEmail($email) {
        return filter_var($email, FILTER_VALIDATE_EMAIL) !== false;
    }

    /**
     * Validate pain level (VAS scale 0-10)
     * @param mixed $painLevel
     * @return bool
     */
    public static function validatePainLevel($painLevel) {
        return is_numeric($painLevel) && $painLevel >= 0 && $painLevel <= 10;
    }

    /**
     * Validate joint count (0-28)
     * @param mixed $jointCount
     * @return bool
     */
    public static function validateJointCount($jointCount) {
        return is_numeric($jointCount) && $jointCount >= 0 && $jointCount <= 28;
    }

    /**
     * Validate fatigue level (FACIT scale 0-10)
     * @param mixed $fatigueLevel
     * @return bool
     */
    public static function validateFatigueLevel($fatigueLevel) {
        return is_numeric($fatigueLevel) && $fatigueLevel >= 0 && $fatigueLevel <= 10;
    }

    /**
     * Validate required field
     * @param mixed $value
     * @return bool
     */
    public static function validateRequired($value) {
        return isset($value) && !empty(trim($value));
    }

    /**
     * Sanitize string input
     * @param string $string
     * @return string
     */
    public static function sanitizeString($string) {
        return htmlspecialchars(strip_tags(trim($string)), ENT_QUOTES, 'UTF-8');
    }

    /**
     * Validate file upload
     * @param array $file $_FILES array
     * @param array $allowedTypes Allowed file extensions
     * @param int $maxSize Maximum file size in bytes
     * @return array ['valid' => bool, 'message' => string]
     */
    public static function validateFileUpload($file, $allowedTypes, $maxSize) {
        // Check if file was uploaded
        if (!isset($file['tmp_name']) || empty($file['tmp_name'])) {
            return array("valid" => false, "message" => "No file uploaded");
        }

        // Check for upload errors
        if ($file['error'] !== UPLOAD_ERR_OK) {
            return array("valid" => false, "message" => "File upload error: " . $file['error']);
        }

        // Check file extension
        $fileName = $file['name'];
        $fileExtension = strtolower(pathinfo($fileName, PATHINFO_EXTENSION));

        if (!in_array($fileExtension, $allowedTypes)) {
            return array("valid" => false, "message" => "Invalid file type. Allowed: " . implode(', ', $allowedTypes));
        }

        // Check file size
        if ($file['size'] > $maxSize) {
            $maxSizeMB = round($maxSize / 1048576, 2);
            return array("valid" => false, "message" => "File too large. Maximum size: " . $maxSizeMB . " MB");
        }

        // Verify it's an actual file
        if (!is_uploaded_file($file['tmp_name'])) {
            return array("valid" => false, "message" => "Invalid file upload");
        }

        return array("valid" => true, "message" => "File valid");
    }

    /**
     * Validate password strength
     * @param string $password
     * @return array ['valid' => bool, 'message' => string]
     */
    public static function validatePassword($password) {
        if (strlen($password) < 8) {
            return array("valid" => false, "message" => "Password must be at least 8 characters");
        }

        if (!preg_match('/[A-Z]/', $password)) {
            return array("valid" => false, "message" => "Password must contain at least one uppercase letter");
        }

        if (!preg_match('/[a-z]/', $password)) {
            return array("valid" => false, "message" => "Password must contain at least one lowercase letter");
        }

        if (!preg_match('/[0-9]/', $password)) {
            return array("valid" => false, "message" => "Password must contain at least one number");
        }

        return array("valid" => true, "message" => "Password valid");
    }

    /**
     * Validate date format (YYYY-MM-DD)
     * @param string $date
     * @return bool
     */
    public static function validateDate($date) {
        $d = DateTime::createFromFormat('Y-m-d', $date);
        return $d && $d->format('Y-m-d') === $date;
    }

    /**
     * Validate datetime format (YYYY-MM-DD HH:MM:SS)
     * @param string $datetime
     * @return bool
     */
    public static function validateDateTime($datetime) {
        $d = DateTime::createFromFormat('Y-m-d H:i:s', $datetime);
        return $d && $d->format('Y-m-d H:i:s') === $datetime;
    }
}
