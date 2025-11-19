package com.myrafriend.models;

/**
 * Assigned Medication Model
 * Represents a medication assigned by doctor to patient
 */
public class Medication {
    private int id;
    private int doctorId;
    private int patientId;
    private int medicationId;
    private String medicationName;
    private String category; // DMARD, Steroid, Supplement, PPI, Biologic, Other
    private String dose;
    private String frequency; // once_daily, twice_daily, weekly, as_needed
    private String timing;
    private String duration;
    private String notes;
    private String commonSideEffects;
    private boolean isActive;
    private String createdAt;

    public Medication() {
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getMedicationId() {
        return medicationId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public String getCategory() {
        return category;
    }

    public String getDose() {
        return dose;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getTiming() {
        return timing;
    }

    public String getDuration() {
        return duration;
    }

    public String getNotes() {
        return notes;
    }

    public String getCommonSideEffects() {
        return commonSideEffects;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // Additional getters for compatibility
    public String getDosage() {
        return dose;
    }

    public int getDurationWeeks() {
        try {
            return Integer.parseInt(duration);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getInstructions() {
        return notes;
    }

    public String getSideEffects() {
        return commonSideEffects;
    }

    public String getPrescribedBy() {
        return "Dr. " + doctorId;
    }

    public String getStatus() {
        return isActive ? "active" : "inactive";
    }

    public String getStartDate() {
        return createdAt;
    }

    public String getEndDate() {
        return createdAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setCommonSideEffects(String commonSideEffects) {
        this.commonSideEffects = commonSideEffects;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
