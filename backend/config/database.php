<?php
/**
 * Database Configuration
 * My RA Friend - MySQL Database Connection
 */

class Database {
    private $host = "localhost";
    private $db_name = "my_ra_friend";
    private $username = "root";
    private $password = "";
    private $conn;

    /**
     * Get database connection
     * @return PDO|null
     */
    public function getConnection() {
        $this->conn = null;

        try {
            $this->conn = new PDO(
                "mysql:host=" . $this->host . ";dbname=" . $this->db_name,
                $this->username,
                $this->password
            );

            // Set PDO attributes
            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $this->conn->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
            $this->conn->exec("set names utf8mb4");

        } catch(PDOException $e) {
            error_log("Database Connection Error: " . $e->getMessage());
            http_response_code(500);
            echo json_encode(array("message" => "Database connection failed"));
            exit();
        }

        return $this->conn;
    }
}
