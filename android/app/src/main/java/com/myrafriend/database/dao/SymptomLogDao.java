package com.myrafriend.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.myrafriend.database.entities.SymptomLogEntity;

import java.util.List;

/**
 * DAO for Symptom Logs
 */
@Dao
public interface SymptomLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(SymptomLogEntity symptomLog);

    @Update
    void update(SymptomLogEntity symptomLog);

    @Query("SELECT * FROM symptom_logs WHERE patient_id = :patientId ORDER BY log_date DESC, created_at DESC")
    LiveData<List<SymptomLogEntity>> getSymptomLogsByPatientId(int patientId);

    @Query("SELECT * FROM symptom_logs WHERE patient_id = :patientId AND log_date >= :startDate AND log_date <= :endDate ORDER BY log_date DESC")
    LiveData<List<SymptomLogEntity>> getSymptomLogsByDateRange(int patientId, String startDate, String endDate);

    @Query("SELECT * FROM symptom_logs WHERE synced = 0")
    List<SymptomLogEntity> getUnsyncedSymptomLogs();

    @Query("UPDATE symptom_logs SET synced = 1 WHERE id = :id")
    void markAsSynced(int id);

    @Query("DELETE FROM symptom_logs WHERE patient_id = :patientId")
    void deleteByPatientId(int patientId);

    @Query("SELECT * FROM symptom_logs WHERE patient_id = :patientId ORDER BY log_date DESC, created_at DESC LIMIT 1")
    SymptomLogEntity getLatestSymptomLog(int patientId);

    @Query("SELECT AVG(pain_level) FROM symptom_logs WHERE patient_id = :patientId AND log_date >= :startDate")
    LiveData<Float> getAveragePainLevel(int patientId, String startDate);

    @Query("SELECT COUNT(*) FROM symptom_logs WHERE patient_id = :patientId AND log_date >= :startDate")
    LiveData<Integer> getSymptomLogCount(int patientId, String startDate);
}
