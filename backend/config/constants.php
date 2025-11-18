<?php
/**
 * Application Constants
 * My RA Friend - Global Configuration
 */

// File Upload Settings
define('MAX_LAB_REPORT_SIZE', 10485760); // 10 MB
define('MAX_MESSAGE_ATTACHMENT_SIZE', 5242880); // 5 MB
define('ALLOWED_LAB_REPORT_TYPES', ['pdf', 'jpg', 'jpeg', 'png']);
define('ALLOWED_MESSAGE_ATTACHMENT_TYPES', ['pdf', 'jpg', 'jpeg', 'png']);

// Upload Directories
define('LAB_REPORTS_DIR', __DIR__ . '/../uploads/lab_reports/');
define('REHAB_VIDEOS_DIR', __DIR__ . '/../uploads/rehab_videos/');
define('MESSAGE_ATTACHMENTS_DIR', __DIR__ . '/../uploads/message_attachments/');

// Notification Settings
define('FCM_SERVER_KEY', 'your_fcm_server_key_here'); // Replace with actual FCM key
define('FCM_API_URL', 'https://fcm.googleapis.com/fcm/send');

// Security Settings
define('MAX_LOGIN_ATTEMPTS', 3);
define('ACCOUNT_LOCKOUT_DURATION', 900); // 15 minutes in seconds

// Pagination
define('DEFAULT_PAGE_SIZE', 50);
define('MAX_PAGE_SIZE', 100);

// Timezone
date_default_timezone_set('UTC');
