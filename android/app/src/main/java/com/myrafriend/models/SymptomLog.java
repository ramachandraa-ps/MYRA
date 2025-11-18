package com.myrafriend.models;

/**
 * Symptom Log Model
 * Represents a patient's symptom tracking entry
 */
public class SymptomLog {
    private int id;
    private int patientId;
    private int painLevel; // 0-10 VAS scale
    private int jointCount; // 0-28
    private String morningStiffness; // less_than_30, 30_minutes, severe_hours
    private int fatigueLevel; // 0-10 FACIT scale
    private boolean swelling;
    private boolean warmth;
    private String functionalAbility; // no_difficulty, mild, moderate, severe
    private String notes;
    private String createdAt;

    public SymptomLog() {
    }

    public SymptomLog(int patientId, int painLevel, int jointCount, String morningStiffness,
                      int fatigueLevel, boolean swelling, boolean warmth,
                      String functionalAbility, String notes) {
        this.patientId = patientId;
        this.painLevel = painLevel;
        this.jointCount = jointCount;
        this.morningStiffness = morningStiffness;
        this.fatigueLevel = fatigueLevel;
        this.swelling = swelling;
        this.warmth = warmth;
        this.functionalAbility = functionalAbility;
        this.notes = notes;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getPainLevel() {
        return painLevel;
    }

    public int getJointCount() {
        return jointCount;
    }

    public String getMorningStiffness() {
        return morningStiffness;
    }

    public int getFatigueLevel() {
        return fatigueLevel;
    }

    public boolean isSwelling() {
        return swelling;
    }

    public boolean isWarmth() {
        return warmth;
    }

    public String getFunctionalAbility() {
        return functionalAbility;
    }

    public String getNotes() {
        return notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setPainLevel(int painLevel) {
        this.painLevel = painLevel;
    }

    public void setJointCount(int jointCount) {
        this.jointCount = jointCount;
    }

    public void setMorningStiffness(String morningStiffness) {
        this.morningStiffness = morningStiffness;
    }

    public void setFatigueLevel(int fatigueLevel) {
        this.fatigueLevel = fatigueLevel;
    }

    public void setSwelling(boolean swelling) {
        this.swelling = swelling;
    }

    public void setWarmth(boolean warmth) {
        this.warmth = warmth;
    }

    public void setFunctionalAbility(String functionalAbility) {
        this.functionalAbility = functionalAbility;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
