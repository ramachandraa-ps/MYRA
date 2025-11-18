<?php
/**
 * Authentication Controller
 * My RA Friend - User Authentication and Session Management
 */

require_once __DIR__ . '/../config/database.php';
require_once __DIR__ . '/../config/constants.php';
require_once __DIR__ . '/../utils/JWTHandler.php';
require_once __DIR__ . '/../utils/Validator.php';

class AuthController {

    private $db;
    private $jwtHandler;

    public function __construct() {
        $database = new Database();
        $this->db = $database->getConnection();
        $this->jwtHandler = new JWTHandler();
    }

    /**
     * User login
     * POST /auth/login
     * Body: {email, password}
     */
    public function login() {
        $data = json_decode(file_get_contents("php://input"));

        // Validate input
        if (empty($data->email) || empty($data->password)) {
            http_response_code(400);
            echo json_encode(array(
                "success" => false,
                "message" => "Email and password are required"
            ));
            return;
        }

        if (!Validator::validateEmail($data->email)) {
            http_response_code(400);
            echo json_encode(array(
                "success" => false,
                "message" => "Invalid email format"
            ));
            return;
        }

        try {
            // Check if account is locked
            $lockQuery = "SELECT id, email, password, role, full_name, is_active,
                          failed_login_attempts, account_locked_until
                          FROM users WHERE email = :email LIMIT 1";
            $lockStmt = $this->db->prepare($lockQuery);
            $lockStmt->bindParam(':email', $data->email);
            $lockStmt->execute();

            if ($lockStmt->rowCount() === 0) {
                http_response_code(401);
                echo json_encode(array(
                    "success" => false,
                    "message" => "Invalid credentials"
                ));
                return;
            }

            $user = $lockStmt->fetch(PDO::FETCH_ASSOC);

            // Check if account is active
            if (!$user['is_active']) {
                http_response_code(403);
                echo json_encode(array(
                    "success" => false,
                    "message" => "Account is deactivated. Please contact administrator."
                ));
                return;
            }

            // Check if account is locked
            if ($user['account_locked_until'] !== null) {
                $lockedUntil = strtotime($user['account_locked_until']);
                if ($lockedUntil > time()) {
                    $remainingMinutes = ceil(($lockedUntil - time()) / 60);
                    http_response_code(423);
                    echo json_encode(array(
                        "success" => false,
                        "message" => "Account is locked due to multiple failed login attempts. Try again in " . $remainingMinutes . " minutes."
                    ));
                    return;
                } else {
                    // Unlock account
                    $this->unlockAccount($user['id']);
                }
            }

            // Verify password
            if (password_verify($data->password, $user['password'])) {
                // Reset failed attempts
                $this->resetFailedAttempts($user['id']);

                // Generate JWT token
                $token = $this->jwtHandler->generateToken($user['id'], $user['role']);

                // Get role-specific ID
                $roleId = $this->getRoleSpecificId($user['id'], $user['role']);

                http_response_code(200);
                echo json_encode(array(
                    "success" => true,
                    "message" => "Login successful",
                    "token" => $token,
                    "user" => array(
                        "id" => $user['id'],
                        "role_id" => $roleId,
                        "email" => $user['email'],
                        "role" => $user['role'],
                        "full_name" => $user['full_name']
                    )
                ));

            } else {
                // Increment failed login attempts
                $this->incrementFailedAttempts($user['id']);

                http_response_code(401);
                echo json_encode(array(
                    "success" => false,
                    "message" => "Invalid credentials"
                ));
            }

        } catch (PDOException $e) {
            error_log("Login Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "An error occurred during login"
            ));
        }
    }

    /**
     * Refresh JWT token
     * POST /auth/refresh
     * Header: Authorization: Bearer {token}
     */
    public function refreshToken() {
        $headers = getallheaders();

        if (!isset($headers['Authorization'])) {
            http_response_code(401);
            echo json_encode(array(
                "success" => false,
                "message" => "Authorization header missing"
            ));
            return;
        }

        $token = str_replace('Bearer ', '', $headers['Authorization']);
        $userData = $this->jwtHandler->validateToken($token);

        if (!$userData) {
            http_response_code(401);
            echo json_encode(array(
                "success" => false,
                "message" => "Invalid or expired token"
            ));
            return;
        }

        // Generate new token
        $newToken = $this->jwtHandler->generateToken($userData['user_id'], $userData['role']);

        http_response_code(200);
        echo json_encode(array(
            "success" => true,
            "token" => $newToken
        ));
    }

    /**
     * Update FCM token
     * POST /auth/update-fcm-token
     * Body: {fcm_token}
     * Header: Authorization required
     */
    public function updateFcmToken($userData) {
        $data = json_decode(file_get_contents("php://input"));

        if (empty($data->fcm_token)) {
            http_response_code(400);
            echo json_encode(array(
                "success" => false,
                "message" => "FCM token is required"
            ));
            return;
        }

        try {
            $query = "UPDATE users SET fcm_token = :fcm_token WHERE id = :user_id";
            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':fcm_token', $data->fcm_token);
            $stmt->bindParam(':user_id', $userData['user_id']);

            if ($stmt->execute()) {
                http_response_code(200);
                echo json_encode(array(
                    "success" => true,
                    "message" => "FCM token updated successfully"
                ));
            } else {
                throw new Exception("Failed to update FCM token");
            }

        } catch (Exception $e) {
            error_log("Update FCM Token Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array(
                "success" => false,
                "message" => "Failed to update FCM token"
            ));
        }
    }

    /**
     * Increment failed login attempts
     * @param int $userId
     */
    private function incrementFailedAttempts($userId) {
        try {
            $query = "UPDATE users
                      SET failed_login_attempts = failed_login_attempts + 1
                      WHERE id = :user_id";
            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':user_id', $userId);
            $stmt->execute();

            // Check if should lock account
            $checkQuery = "SELECT failed_login_attempts FROM users WHERE id = :user_id";
            $checkStmt = $this->db->prepare($checkQuery);
            $checkStmt->bindParam(':user_id', $userId);
            $checkStmt->execute();
            $row = $checkStmt->fetch(PDO::FETCH_ASSOC);

            if ($row['failed_login_attempts'] >= MAX_LOGIN_ATTEMPTS) {
                $this->lockAccount($userId);
            }

        } catch (PDOException $e) {
            error_log("Increment Failed Attempts Error: " . $e->getMessage());
        }
    }

    /**
     * Reset failed login attempts
     * @param int $userId
     */
    private function resetFailedAttempts($userId) {
        try {
            $query = "UPDATE users
                      SET failed_login_attempts = 0,
                          account_locked_until = NULL
                      WHERE id = :user_id";
            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':user_id', $userId);
            $stmt->execute();
        } catch (PDOException $e) {
            error_log("Reset Failed Attempts Error: " . $e->getMessage());
        }
    }

    /**
     * Lock account
     * @param int $userId
     */
    private function lockAccount($userId) {
        try {
            $lockUntil = date('Y-m-d H:i:s', time() + ACCOUNT_LOCKOUT_DURATION);
            $query = "UPDATE users SET account_locked_until = :lock_until WHERE id = :user_id";
            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':lock_until', $lockUntil);
            $stmt->bindParam(':user_id', $userId);
            $stmt->execute();
        } catch (PDOException $e) {
            error_log("Lock Account Error: " . $e->getMessage());
        }
    }

    /**
     * Unlock account
     * @param int $userId
     */
    private function unlockAccount($userId) {
        try {
            $query = "UPDATE users
                      SET account_locked_until = NULL,
                          failed_login_attempts = 0
                      WHERE id = :user_id";
            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':user_id', $userId);
            $stmt->execute();
        } catch (PDOException $e) {
            error_log("Unlock Account Error: " . $e->getMessage());
        }
    }

    /**
     * Get role-specific ID (patient_id or doctor_id)
     * @param int $userId
     * @param string $role
     * @return int|null
     */
    private function getRoleSpecificId($userId, $role) {
        try {
            if ($role === 'patient') {
                $query = "SELECT id FROM patients WHERE user_id = :user_id LIMIT 1";
            } elseif ($role === 'doctor') {
                $query = "SELECT id FROM doctors WHERE user_id = :user_id LIMIT 1";
            } else {
                return null;
            }

            $stmt = $this->db->prepare($query);
            $stmt->bindParam(':user_id', $userId);
            $stmt->execute();

            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            return $row ? $row['id'] : null;

        } catch (PDOException $e) {
            error_log("Get Role Specific ID Error: " . $e->getMessage());
            return null;
        }
    }
}
