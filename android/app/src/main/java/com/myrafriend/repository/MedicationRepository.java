package com.myrafriend.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.myrafriend.database.AppDatabase;
import com.myrafriend.database.dao.MedicationDao;
import com.myrafriend.database.dao.MedicationIntakeDao;
import com.myrafriend.database.entities.MedicationEntity;
import com.myrafriend.database.entities.MedicationIntakeEntity;
import com.myrafriend.models.Medication;
import com.myrafriend.network.ApiResponse;
import com.myrafriend.network.ApiService;
import com.myrafriend.network.RetrofitClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Medication Repository
 * Handles medication management and intake tracking with offline caching
 */
public class MedicationRepository {
    private static final String TAG = "MedicationRepository";
    private final ApiService apiService;
    private final MedicationDao medicationDao;
    private final MedicationIntakeDao medicationIntakeDao;
    private final RetrofitClient retrofitClient;

    public MedicationRepository(Context context) {
        retrofitClient = RetrofitClient.getInstance(context);
        apiService = retrofitClient.getApiService();
        AppDatabase database = AppDatabase.getInstance(context);
        medicationDao = database.medicationDao();
        medicationIntakeDao = database.medicationIntakeDao();
    }

    /**
     * Get assigned medications (with offline caching)
     */
    public LiveData<List<MedicationEntity>> getAssignedMedications(int patientId) {
        // Fetch from server and update local cache
        fetchAndCacheMedications(patientId);

        // Return LiveData from local database
        return medicationDao.getActiveMedications(patientId);
    }

    /**
     * Fetch medications from server and cache locally
     */
    private void fetchAndCacheMedications(int patientId) {
        apiService.getAssignedMedications(patientId).enqueue(new Callback<ApiResponse<List<Medication>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Medication>>> call,
                                   @NonNull Response<ApiResponse<List<Medication>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Medication>> apiResponse = response.body();

                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Update local cache
                        new Thread(() -> {
                            List<Medication> medications = apiResponse.getData();
                            for (Medication med : medications) {
                                MedicationEntity entity = convertToEntity(med);
                                medicationDao.insert(entity);
                            }
                        }).start();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Medication>>> call, @NonNull Throwable t) {
                Log.e(TAG, "Fetch medications error: " + t.getMessage());
                // Data will be served from local cache
            }
        });
    }

    /**
     * Record medication intake
     */
    public LiveData<AuthRepository.Resource<Void>> recordMedicationIntake(
            int assignedMedicationId, String status, String notes) {
        MutableLiveData<AuthRepository.Resource<Void>> result = new MutableLiveData<>();
        result.setValue(AuthRepository.Resource.loading(null));

        // Save to local database first
        new Thread(() -> {
            MedicationIntakeEntity entity = new MedicationIntakeEntity();
            entity.setAssignedMedicationId(assignedMedicationId);
            entity.setPatientId(retrofitClient.getUserId());
            entity.setStatus(status);
            entity.setNotes(notes);
            entity.setIntakeDate(getCurrentDate());
            entity.setIntakeTime(getCurrentTime());
            entity.setSynced(false);

            long id = medicationIntakeDao.insert(entity);

            // Then sync to server
            java.util.Map<String, Object> intakeData = new java.util.HashMap<>();
            intakeData.put("assigned_medication_id", assignedMedicationId);
            intakeData.put("status", status);
            intakeData.put("notes", notes);

            apiService.recordMedicationIntake(intakeData)
                    .enqueue(new Callback<ApiResponse<Void>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<Void>> call,
                                       @NonNull Response<ApiResponse<Void>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<Void> apiResponse = response.body();

                        if (apiResponse.isSuccess()) {
                            // Mark as synced
                            new Thread(() -> medicationIntakeDao.markAsSynced((int) id)).start();
                            result.postValue(AuthRepository.Resource.success(null));
                        } else {
                            result.postValue(AuthRepository.Resource.error(apiResponse.getMessage(), null));
                        }
                    } else {
                        result.postValue(AuthRepository.Resource.error("Failed to record intake", null));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                    Log.e(TAG, "Record intake error: " + t.getMessage());
                    // Data is saved locally, will sync later
                    result.postValue(AuthRepository.Resource.success(null));
                }
            });
        }).start();

        return result;
    }

    /**
     * Get adherence metrics
     */
    public LiveData<AuthRepository.Resource<Map<String, Object>>> getAdherenceMetrics(int patientId) {
        MutableLiveData<AuthRepository.Resource<Map<String, Object>>> result = new MutableLiveData<>();
        result.setValue(AuthRepository.Resource.loading(null));

        apiService.getAdherenceMetrics(patientId).enqueue(new Callback<ApiResponse<Map<String, Object>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Map<String, Object>>> call,
                                   @NonNull Response<ApiResponse<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Map<String, Object>> apiResponse = response.body();

                    if (apiResponse.isSuccess()) {
                        result.setValue(AuthRepository.Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(AuthRepository.Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(AuthRepository.Resource.error("Failed to get adherence metrics", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Map<String, Object>>> call, @NonNull Throwable t) {
                Log.e(TAG, "Adherence metrics error: " + t.getMessage());
                result.setValue(AuthRepository.Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Get medication intake logs from local database
     */
    public LiveData<List<MedicationIntakeEntity>> getIntakeLogs(int patientId) {
        return medicationIntakeDao.getIntakeLogsByPatientId(patientId);
    }

    /**
     * Get today's intake logs
     */
    public LiveData<List<MedicationIntakeEntity>> getTodayIntakeLogs(int patientId) {
        return medicationIntakeDao.getTodayIntakeLogs(patientId, getCurrentDate());
    }

    /**
     * Convert Medication to MedicationEntity
     */
    private MedicationEntity convertToEntity(Medication medication) {
        MedicationEntity entity = new MedicationEntity();
        entity.setId(medication.getId());
        entity.setPatientId(medication.getPatientId());
        entity.setMedicationName(medication.getMedicationName());
        entity.setDosage(medication.getDosage());
        entity.setFrequency(medication.getFrequency());
        entity.setTiming(medication.getTiming());
        entity.setDurationWeeks(medication.getDurationWeeks());
        entity.setInstructions(medication.getInstructions());
        entity.setSideEffects(medication.getSideEffects());
        entity.setPrescribedBy(medication.getPrescribedBy());
        entity.setStatus(medication.getStatus());
        entity.setStartDate(medication.getStartDate());
        entity.setEndDate(medication.getEndDate());
        return entity;
    }

    /**
     * Get current date in YYYY-MM-DD format
     */
    private String getCurrentDate() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(new java.util.Date());
    }

    /**
     * Get current time in HH:mm:ss format
     */
    private String getCurrentTime() {
        return new java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
                .format(new java.util.Date());
    }
}
