package com.myrafriend.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.myrafriend.database.entities.SymptomLogEntity;
import com.myrafriend.models.SymptomLog;
import com.myrafriend.repository.AuthRepository;
import com.myrafriend.repository.SymptomRepository;

import java.util.List;

/**
 * Symptom ViewModel
 * Manages UI state for symptom logging screens
 */
public class SymptomViewModel extends AndroidViewModel {
    private final SymptomRepository symptomRepository;

    public SymptomViewModel(@NonNull Application application) {
        super(application);
        symptomRepository = new SymptomRepository(application);
    }

    /**
     * Submit symptom log
     */
    public LiveData<AuthRepository.Resource<Void>> submitSymptomLog(SymptomLog symptomLog) {
        return symptomRepository.submitSymptomLog(symptomLog);
    }

    /**
     * Get symptom history
     */
    public LiveData<List<SymptomLogEntity>> getSymptomHistory(int patientId) {
        return symptomRepository.getSymptomHistory(patientId);
    }

    /**
     * Get symptom logs by date range
     */
    public LiveData<List<SymptomLogEntity>> getSymptomLogsByDateRange(
            int patientId, String startDate, String endDate) {
        return symptomRepository.getSymptomLogsByDateRange(patientId, startDate, endDate);
    }

    /**
     * Get average pain level
     */
    public LiveData<Float> getAveragePainLevel(int patientId, String startDate) {
        return symptomRepository.getAveragePainLevel(patientId, startDate);
    }

    /**
     * Report flare
     */
    public LiveData<AuthRepository.Resource<Void>> reportFlare(
            int patientId, String severity, String notes) {
        return symptomRepository.reportFlare(patientId, severity, notes);
    }
}
