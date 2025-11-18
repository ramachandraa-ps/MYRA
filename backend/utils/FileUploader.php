<?php
/**
 * File Uploader Utility
 * My RA Friend - File Upload Handler
 */

require_once __DIR__ . '/../config/constants.php';
require_once __DIR__ . '/Validator.php';

class FileUploader {

    /**
     * Upload lab report file
     * @param array $file $_FILES array
     * @param int $patientId Patient ID for filename
     * @return array ['success' => bool, 'file_path' => string, 'message' => string]
     */
    public function uploadLabReport($file, $patientId) {
        $validation = Validator::validateFileUpload(
            $file,
            ALLOWED_LAB_REPORT_TYPES,
            MAX_LAB_REPORT_SIZE
        );

        if (!$validation['valid']) {
            return array(
                "success" => false,
                "message" => $validation['message']
            );
        }

        return $this->saveFile($file, LAB_REPORTS_DIR, $patientId, 'lab');
    }

    /**
     * Upload message attachment file
     * @param array $file $_FILES array
     * @param int $userId User ID for filename
     * @return array ['success' => bool, 'file_path' => string, 'message' => string]
     */
    public function uploadMessageAttachment($file, $userId) {
        $validation = Validator::validateFileUpload(
            $file,
            ALLOWED_MESSAGE_ATTACHMENT_TYPES,
            MAX_MESSAGE_ATTACHMENT_SIZE
        );

        if (!$validation['valid']) {
            return array(
                "success" => false,
                "message" => $validation['message']
            );
        }

        return $this->saveFile($file, MESSAGE_ATTACHMENTS_DIR, $userId, 'msg');
    }

    /**
     * Save uploaded file to destination
     * @param array $file $_FILES array
     * @param string $uploadDir Destination directory
     * @param int $userId User/Patient ID
     * @param string $prefix Filename prefix
     * @return array ['success' => bool, 'file_path' => string, 'message' => string]
     */
    private function saveFile($file, $uploadDir, $userId, $prefix) {
        // Ensure upload directory exists
        if (!file_exists($uploadDir)) {
            if (!mkdir($uploadDir, 0777, true)) {
                return array(
                    "success" => false,
                    "message" => "Failed to create upload directory"
                );
            }
        }

        // Generate unique filename
        $fileExtension = strtolower(pathinfo($file['name'], PATHINFO_EXTENSION));
        $fileName = $prefix . '_' . $userId . '_' . time() . '_' . uniqid() . '.' . $fileExtension;
        $filePath = $uploadDir . $fileName;

        // Move uploaded file
        if (move_uploaded_file($file['tmp_name'], $filePath)) {
            // Return relative path for database storage
            $relativePath = str_replace(__DIR__ . '/../', '', $filePath);
            return array(
                "success" => true,
                "file_path" => $relativePath,
                "message" => "File uploaded successfully"
            );
        } else {
            return array(
                "success" => false,
                "message" => "Failed to move uploaded file"
            );
        }
    }

    /**
     * Delete file
     * @param string $filePath File path to delete
     * @return bool
     */
    public function deleteFile($filePath) {
        $fullPath = __DIR__ . '/../' . $filePath;
        if (file_exists($fullPath)) {
            return unlink($fullPath);
        }
        return false;
    }
}
