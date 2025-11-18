package com.myrafriend.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myrafriend.database.entities.MedicationIntakeEntity;

import java.util.List;

/**
 * DAO for Medication Intake Logs
 */
@Dao
public interface MedicationIntakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(MedicationIntakeEntity intakeLog);

    @Query("SELECT * FROM medication_intake_logs WHERE patient_id = :patientId ORDER BY intake_date DESC, intake_time DESC")
    LiveData<List<MedicationIntakeEntity>> getIntakeLogsByPatientId(int patientId);

    @Query("SELECT * FROM medication_intake_logs WHERE assigned_medication_id = :medicationId ORDER BY intake_date DESC")
    LiveData<List<MedicationIntakeEntity>> getIntakeLogsByMedicationId(int medicationId);

    @Query("SELECT * FROM medication_intake_logs WHERE patient_id = :patientId AND intake_date >= :startDate AND intake_date <= :endDate")
    LiveData<List<MedicationIntakeEntity>> getIntakeLogsByDateRange(int patientId, String startDate, String endDate);

    @Query("SELECT * FROM medication_intake_logs WHERE synced = 0")
    List<MedicationIntakeEntity> getUnsyncedIntakeLogs();

    @Query("UPDATE medication_intake_logs SET synced = 1 WHERE id = :id")
    void markAsSynced(int id);

    @Query("DELETE FROM medication_intake_logs WHERE patient_id = :patientId")
    void deleteByPatientId(int patientId);

    @Query("SELECT COUNT(*) FROM medication_intake_logs WHERE assigned_medication_id = :medicationId AND status = 'taken' AND intake_date >= :startDate")
    LiveData<Integer> getTakenCount(int medicationId, String startDate);

    @Query("SELECT COUNT(*) FROM medication_intake_logs WHERE assigned_medication_id = :medicationId AND intake_date >= :startDate")
    LiveData<Integer> getTotalDoses(int medicationId, String startDate);

    @Query("SELECT * FROM medication_intake_logs WHERE patient_id = :patientId AND intake_date = :date")
    LiveData<List<MedicationIntakeEntity>> getTodayIntakeLogs(int patientId, String date);
}
