package com.myrafriend.models;

/**
 * User Model
 * Represents a user in the My RA Friend system
 */
public class User {
    private int id;
    private int roleId; // patient_id or doctor_id based on role
    private String email;
    private String role; // patient, doctor, admin
    private String fullName;
    private String phone;
    private String fcmToken;
    private boolean isActive;

    public User() {
    }

    public User(int id, int roleId, String email, String role, String fullName) {
        this.id = id;
        this.roleId = roleId;
        this.email = email;
        this.role = role;
        this.fullName = fullName;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public boolean isActive() {
        return isActive;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
