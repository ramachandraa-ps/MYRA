package com.myrafriend.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.myrafriend.repository.AuthRepository;
import com.myrafriend.repository.DoctorRepository;

import java.util.List;
import java.util.Map;

/**
 * Doctor ViewModel
 * Manages UI state for doctor screens
 */
public class DoctorViewModel extends AndroidViewModel {
    private final DoctorRepository doctorRepository;

    public DoctorViewModel(@NonNull Application application) {
        super(application);
        doctorRepository = new DoctorRepository(application);
    }

    /**
     * Get assigned patients
     */
    public LiveData<AuthRepository.Resource<List<Map<String, Object>>>> getAssignedPatients(int doctorId) {
        return doctorRepository.getAssignedPatients(doctorId);
    }

    /**
     * Get patient detail
     */
    public LiveData<AuthRepository.Resource<Map<String, Object>>> getPatientDetail(int patientId) {
        return doctorRepository.getPatientDetail(patientId);
    }

    /**
     * Prescribe medication
     */
    public LiveData<AuthRepository.Resource<Void>> prescribeMedication(
            int patientId, int medicationId, String dosage, String frequency,
            String timing, int durationWeeks, String instructions) {
        return doctorRepository.prescribeMedication(
                patientId, medicationId, dosage, frequency, timing, durationWeeks, instructions);
    }

    /**
     * Assign rehab exercise
     */
    public LiveData<AuthRepository.Resource<Void>> assignRehab(
            int patientId, int rehabId, int frequency, int duration, String instructions) {
        return doctorRepository.assignRehab(patientId, rehabId, frequency, duration, instructions);
    }

    /**
     * Get medications master list
     */
    public LiveData<AuthRepository.Resource<List<Map<String, Object>>>> getMedicationsMaster() {
        return doctorRepository.getMedicationsMaster();
    }

    /**
     * Get rehab exercises master list
     */
    public LiveData<AuthRepository.Resource<List<Map<String, Object>>>> getRehabExercisesMaster() {
        return doctorRepository.getRehabExercisesMaster();
    }
}
