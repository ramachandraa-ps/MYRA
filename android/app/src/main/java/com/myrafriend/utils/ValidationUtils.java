package com.myrafriend.utils;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Validation Utility Class
 * Provides input validation methods
 */
public class ValidationUtils {

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Validate password strength
     * At least 6 characters, contains letter and number
     */
    public static boolean isValidPassword(String password) {
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            return false;
        }
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        return hasLetter && hasDigit;
    }

    /**
     * Validate pain level (0-10)
     */
    public static boolean isValidPainLevel(int painLevel) {
        return painLevel >= 0 && painLevel <= 10;
    }

    /**
     * Validate joint count (0-28)
     */
    public static boolean isValidJointCount(int jointCount) {
        return jointCount >= 0 && jointCount <= 28;
    }

    /**
     * Validate fatigue level (0-10)
     */
    public static boolean isValidFatigueLevel(int fatigueLevel) {
        return fatigueLevel >= 0 && fatigueLevel <= 10;
    }

    /**
     * Validate required field
     */
    public static boolean isNotEmpty(String text) {
        return !TextUtils.isEmpty(text) && !text.trim().isEmpty();
    }

    /**
     * Validate phone number
     */
    public static boolean isValidPhone(String phone) {
        return !TextUtils.isEmpty(phone) &&
               Patterns.PHONE.matcher(phone).matches() &&
               phone.length() >= 10;
    }

    /**
     * Get password strength message
     */
    public static String getPasswordStrengthMessage(String password) {
        if (TextUtils.isEmpty(password)) {
            return "Password is required";
        }
        if (password.length() < 6) {
            return "Password must be at least 6 characters";
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            return "Password must contain at least one letter";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one number";
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            return "Strong password - consider adding special characters";
        }
        return "Strong password";
    }
}
