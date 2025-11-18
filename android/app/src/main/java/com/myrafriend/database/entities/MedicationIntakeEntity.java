package com.myrafriend.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room Entity for Medication Intake Logs
 */
@Entity(tableName = "medication_intake_logs")
public class MedicationIntakeEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "assigned_medication_id")
    private int assignedMedicationId;

    @ColumnInfo(name = "patient_id")
    private int patientId;

    @ColumnInfo(name = "medication_name")
    private String medicationName;

    @ColumnInfo(name = "status")
    private String status; // taken, skipped, missed

    @ColumnInfo(name = "intake_date")
    private String intakeDate;

    @ColumnInfo(name = "intake_time")
    private String intakeTime;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "synced")
    private boolean synced;

    // Constructor
    public MedicationIntakeEntity() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssignedMedicationId() {
        return assignedMedicationId;
    }

    public void setAssignedMedicationId(int assignedMedicationId) {
        this.assignedMedicationId = assignedMedicationId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIntakeDate() {
        return intakeDate;
    }

    public void setIntakeDate(String intakeDate) {
        this.intakeDate = intakeDate;
    }

    public String getIntakeTime() {
        return intakeTime;
    }

    public void setIntakeTime(String intakeTime) {
        this.intakeTime = intakeTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }
}
