<?php
/**
 * Role Middleware
 * My RA Friend - Role-Based Access Control
 */

class RoleMiddleware {

    /**
     * Check if user has required role
     * @param array $userData User data from JWT
     * @param string|array $allowedRoles Single role or array of allowed roles
     * @return bool True if authorized, exits with 403 if not
     */
    public static function checkRole($userData, $allowedRoles) {
        if (!isset($userData['role'])) {
            self::forbiddenResponse("Role information missing");
        }

        $userRole = $userData['role'];

        // Convert single role to array for uniform handling
        if (!is_array($allowedRoles)) {
            $allowedRoles = array($allowedRoles);
        }

        if (!in_array($userRole, $allowedRoles)) {
            self::forbiddenResponse("Access denied for this role");
        }

        return true;
    }

    /**
     * Send forbidden response and exit
     * @param string $message
     */
    private static function forbiddenResponse($message) {
        http_response_code(403);
        echo json_encode(array(
            "success" => false,
            "message" => $message
        ));
        exit();
    }
}
