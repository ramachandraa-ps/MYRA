<?php
/**
 * Authentication Middleware
 * My RA Friend - JWT Token Validation Middleware
 */

require_once __DIR__ . '/../utils/JWTHandler.php';

class AuthMiddleware {

    /**
     * Authenticate user from Authorization header
     * @return array User data (user_id, role) or exits with 401
     */
    public static function authenticate() {
        $headers = getallheaders();

        if (!isset($headers['Authorization'])) {
            self::unauthorizedResponse("Authorization header missing");
        }

        $authHeader = $headers['Authorization'];
        $token = str_replace('Bearer ', '', $authHeader);

        if (empty($token)) {
            self::unauthorizedResponse("Token missing");
        }

        $jwtHandler = new JWTHandler();
        $userData = $jwtHandler->validateToken($token);

        if (!$userData) {
            self::unauthorizedResponse("Invalid or expired token");
        }

        return $userData;
    }

    /**
     * Send unauthorized response and exit
     * @param string $message
     */
    private static function unauthorizedResponse($message) {
        http_response_code(401);
        echo json_encode(array(
            "success" => false,
            "message" => $message
        ));
        exit();
    }
}
