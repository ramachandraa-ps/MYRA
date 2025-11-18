<?php
/**
 * JWT Configuration
 * My RA Friend - JWT Token Settings
 */

define('JWT_SECRET_KEY', 'myrafriend_secret_key_2025_change_in_production');
define('JWT_ISSUER', 'my-ra-friend-api');
define('JWT_AUDIENCE', 'my-ra-friend-app');
define('JWT_EXPIRATION_TIME', 86400); // 24 hours in seconds
