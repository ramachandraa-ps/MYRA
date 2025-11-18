<?php
/**
 * JWT Handler
 * My RA Friend - JWT Token Generation and Validation
 *
 * Note: This is a simple JWT implementation for MVP.
 * For production, consider using firebase/php-jwt library via Composer.
 */

require_once __DIR__ . '/../config/jwt.php';

class JWTHandler {

    /**
     * Generate JWT token
     * @param int $user_id
     * @param string $role
     * @return string
     */
    public function generateToken($user_id, $role) {
        $issued_at = time();
        $expiration = $issued_at + JWT_EXPIRATION_TIME;

        $header = json_encode(['typ' => 'JWT', 'alg' => 'HS256']);

        $payload = json_encode([
            'iss' => JWT_ISSUER,
            'aud' => JWT_AUDIENCE,
            'iat' => $issued_at,
            'exp' => $expiration,
            'data' => [
                'user_id' => $user_id,
                'role' => $role
            ]
        ]);

        $base64UrlHeader = $this->base64UrlEncode($header);
        $base64UrlPayload = $this->base64UrlEncode($payload);

        $signature = hash_hmac('sha256',
            $base64UrlHeader . "." . $base64UrlPayload,
            JWT_SECRET_KEY,
            true
        );
        $base64UrlSignature = $this->base64UrlEncode($signature);

        return $base64UrlHeader . "." . $base64UrlPayload . "." . $base64UrlSignature;
    }

    /**
     * Validate JWT token
     * @param string $token
     * @return array|false User data array or false if invalid
     */
    public function validateToken($token) {
        try {
            $tokenParts = explode('.', $token);

            if (count($tokenParts) !== 3) {
                return false;
            }

            list($base64UrlHeader, $base64UrlPayload, $base64UrlSignature) = $tokenParts;

            // Verify signature
            $signature = $this->base64UrlDecode($base64UrlSignature);
            $expectedSignature = hash_hmac('sha256',
                $base64UrlHeader . "." . $base64UrlPayload,
                JWT_SECRET_KEY,
                true
            );

            if (!hash_equals($signature, $expectedSignature)) {
                return false;
            }

            // Decode payload
            $payload = json_decode($this->base64UrlDecode($base64UrlPayload), true);

            // Check expiration
            if (isset($payload['exp']) && $payload['exp'] < time()) {
                return false;
            }

            // Return user data
            return isset($payload['data']) ? $payload['data'] : false;

        } catch (Exception $e) {
            error_log("JWT Validation Error: " . $e->getMessage());
            return false;
        }
    }

    /**
     * Base64 URL encode
     * @param string $data
     * @return string
     */
    private function base64UrlEncode($data) {
        return rtrim(strtr(base64_encode($data), '+/', '-_'), '=');
    }

    /**
     * Base64 URL decode
     * @param string $data
     * @return string
     */
    private function base64UrlDecode($data) {
        return base64_decode(strtr($data, '-_', '+/'));
    }
}
