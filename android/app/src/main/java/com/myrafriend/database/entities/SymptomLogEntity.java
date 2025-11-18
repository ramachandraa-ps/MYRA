package com.myrafriend.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room Entity for Symptom Logs
 */
@Entity(tableName = "symptom_logs")
public class SymptomLogEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "patient_id")
    private int patientId;

    @ColumnInfo(name = "pain_level")
    private int painLevel;

    @ColumnInfo(name = "joint_count")
    private int jointCount;

    @ColumnInfo(name = "morning_stiffness")
    private String morningStiffness;

    @ColumnInfo(name = "fatigue_level")
    private int fatigueLevel;

    @ColumnInfo(name = "swelling")
    private boolean swelling;

    @ColumnInfo(name = "warmth")
    private boolean warmth;

    @ColumnInfo(name = "functional_ability")
    private String functionalAbility;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "log_date")
    private String logDate;

    @ColumnInfo(name = "created_at")
    private String createdAt;

    @ColumnInfo(name = "synced")
    private boolean synced;

    // Constructor
    public SymptomLogEntity() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getPainLevel() {
        return painLevel;
    }

    public void setPainLevel(int painLevel) {
        this.painLevel = painLevel;
    }

    public int getJointCount() {
        return jointCount;
    }

    public void setJointCount(int jointCount) {
        this.jointCount = jointCount;
    }

    public String getMorningStiffness() {
        return morningStiffness;
    }

    public void setMorningStiffness(String morningStiffness) {
        this.morningStiffness = morningStiffness;
    }

    public int getFatigueLevel() {
        return fatigueLevel;
    }

    public void setFatigueLevel(int fatigueLevel) {
        this.fatigueLevel = fatigueLevel;
    }

    public boolean isSwelling() {
        return swelling;
    }

    public void setSwelling(boolean swelling) {
        this.swelling = swelling;
    }

    public boolean isWarmth() {
        return warmth;
    }

    public void setWarmth(boolean warmth) {
        this.warmth = warmth;
    }

    public String getFunctionalAbility() {
        return functionalAbility;
    }

    public void setFunctionalAbility(String functionalAbility) {
        this.functionalAbility = functionalAbility;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }
}
