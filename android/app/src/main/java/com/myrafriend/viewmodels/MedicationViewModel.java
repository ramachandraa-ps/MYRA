package com.myrafriend.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.myrafriend.database.entities.MedicationEntity;
import com.myrafriend.database.entities.MedicationIntakeEntity;
import com.myrafriend.repository.AuthRepository;
import com.myrafriend.repository.MedicationRepository;

import java.util.List;
import java.util.Map;

/**
 * Medication ViewModel
 * Manages UI state for medication screens
 */
public class MedicationViewModel extends AndroidViewModel {
    private final MedicationRepository medicationRepository;

    public MedicationViewModel(@NonNull Application application) {
        super(application);
        medicationRepository = new MedicationRepository(application);
    }

    /**
     * Get assigned medications
     */
    public LiveData<List<MedicationEntity>> getAssignedMedications(int patientId) {
        return medicationRepository.getAssignedMedications(patientId);
    }

    /**
     * Record medication intake
     */
    public LiveData<AuthRepository.Resource<Void>> recordMedicationIntake(
            int assignedMedicationId, String status, String notes) {
        return medicationRepository.recordMedicationIntake(assignedMedicationId, status, notes);
    }

    /**
     * Get adherence metrics
     */
    public LiveData<AuthRepository.Resource<Map<String, Object>>> getAdherenceMetrics(int patientId) {
        return medicationRepository.getAdherenceMetrics(patientId);
    }

    /**
     * Get intake logs
     */
    public LiveData<List<MedicationIntakeEntity>> getIntakeLogs(int patientId) {
        return medicationRepository.getIntakeLogs(patientId);
    }

    /**
     * Get today's intake logs
     */
    public LiveData<List<MedicationIntakeEntity>> getTodayIntakeLogs(int patientId) {
        return medicationRepository.getTodayIntakeLogs(patientId);
    }
}
