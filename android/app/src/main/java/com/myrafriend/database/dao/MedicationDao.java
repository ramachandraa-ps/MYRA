package com.myrafriend.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.myrafriend.database.entities.MedicationEntity;

import java.util.List;

/**
 * DAO for Medications
 */
@Dao
public interface MedicationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MedicationEntity medication);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MedicationEntity> medications);

    @Update
    void update(MedicationEntity medication);

    @Query("SELECT * FROM medications WHERE patient_id = :patientId AND status = 'active' ORDER BY start_date DESC")
    LiveData<List<MedicationEntity>> getActiveMedications(int patientId);

    @Query("SELECT * FROM medications WHERE patient_id = :patientId ORDER BY start_date DESC")
    LiveData<List<MedicationEntity>> getAllMedications(int patientId);

    @Query("SELECT * FROM medications WHERE id = :medicationId")
    LiveData<MedicationEntity> getMedicationById(int medicationId);

    @Query("DELETE FROM medications WHERE patient_id = :patientId")
    void deleteByPatientId(int patientId);

    @Query("UPDATE medications SET status = :status WHERE id = :medicationId")
    void updateMedicationStatus(int medicationId, String status);

    @Query("SELECT COUNT(*) FROM medications WHERE patient_id = :patientId AND status = 'active'")
    LiveData<Integer> getActiveMedicationCount(int patientId);
}
