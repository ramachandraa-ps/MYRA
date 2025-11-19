package com.myrafriend.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.myrafriend.database.AppDatabase;
import com.myrafriend.database.dao.SymptomLogDao;
import com.myrafriend.database.entities.SymptomLogEntity;
import com.myrafriend.models.SymptomLog;
import com.myrafriend.network.ApiResponse;
import com.myrafriend.network.ApiService;
import com.myrafriend.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Symptom Repository
 * Handles symptom logging and retrieval with offline caching
 */
public class SymptomRepository {
    private static final String TAG = "SymptomRepository";
    private final ApiService apiService;
    private final SymptomLogDao symptomLogDao;
    private final RetrofitClient retrofitClient;

    public SymptomRepository(Context context) {
        retrofitClient = RetrofitClient.getInstance(context);
        apiService = retrofitClient.getApiService();
        AppDatabase database = AppDatabase.getInstance(context);
        symptomLogDao = database.symptomLogDao();
    }

    /**
     * Submit symptom log to server and cache locally
     */
    public LiveData<AuthRepository.Resource<Void>> submitSymptomLog(SymptomLog symptomLog) {
        MutableLiveData<AuthRepository.Resource<Void>> result = new MutableLiveData<>();
        result.setValue(AuthRepository.Resource.loading(null));

        // Save to local database first
        new Thread(() -> {
            SymptomLogEntity entity = convertToEntity(symptomLog);
            entity.setSynced(false);
            long id = symptomLogDao.insert(entity);

            // Then sync to server
            apiService.submitSymptomLog(symptomLog).enqueue(new Callback<ApiResponse<Void>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<Void>> call,
                                       @NonNull Response<ApiResponse<Void>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<Void> apiResponse = response.body();

                        if (apiResponse.isSuccess()) {
                            // Mark as synced
                            new Thread(() -> symptomLogDao.markAsSynced((int) id)).start();
                            result.postValue(AuthRepository.Resource.success(null));
                        } else {
                            result.postValue(AuthRepository.Resource.error(apiResponse.getMessage(), null));
                        }
                    } else {
                        result.postValue(AuthRepository.Resource.error("Failed to submit symptom log", null));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                    Log.e(TAG, "Submit symptom log error: " + t.getMessage());
                    // Data is saved locally, will sync later
                    result.postValue(AuthRepository.Resource.success(null));
                }
            });
        }).start();

        return result;
    }

    /**
     * Get symptom history from local database (auto-synced via Room LiveData)
     */
    public LiveData<List<SymptomLogEntity>> getSymptomHistory(int patientId) {
        return symptomLogDao.getSymptomLogsByPatientId(patientId);
    }

    /**
     * Get symptom logs by date range
     */
    public LiveData<List<SymptomLogEntity>> getSymptomLogsByDateRange(int patientId, String startDate, String endDate) {
        return symptomLogDao.getSymptomLogsByDateRange(patientId, startDate, endDate);
    }

    /**
     * Get average pain level
     */
    public LiveData<Float> getAveragePainLevel(int patientId, String startDate) {
        return symptomLogDao.getAveragePainLevel(patientId, startDate);
    }

    /**
     * Report flare to doctor
     */
    public LiveData<AuthRepository.Resource<Void>> reportFlare(int patientId, String severity, String notes) {
        MutableLiveData<AuthRepository.Resource<Void>> result = new MutableLiveData<>();
        result.setValue(AuthRepository.Resource.loading(null));

        java.util.Map<String, Object> flareData = new java.util.HashMap<>();
        flareData.put("patient_id", patientId);
        flareData.put("severity", severity);
        flareData.put("notes", notes);

        apiService.reportFlare(flareData).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Void>> call,
                                   @NonNull Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Void> apiResponse = response.body();

                    if (apiResponse.isSuccess()) {
                        result.setValue(AuthRepository.Resource.success(null));
                    } else {
                        result.setValue(AuthRepository.Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(AuthRepository.Resource.error("Failed to report flare", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                Log.e(TAG, "Report flare error: " + t.getMessage());
                result.setValue(AuthRepository.Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Convert SymptomLog to SymptomLogEntity
     */
    private SymptomLogEntity convertToEntity(SymptomLog symptomLog) {
        SymptomLogEntity entity = new SymptomLogEntity();
        entity.setPatientId(symptomLog.getPatientId());
        entity.setPainLevel(symptomLog.getPainLevel());
        entity.setJointCount(symptomLog.getJointCount());
        entity.setMorningStiffness(symptomLog.getMorningStiffness());
        entity.setFatigueLevel(symptomLog.getFatigueLevel());
        entity.setSwelling(symptomLog.isSwelling());
        entity.setWarmth(symptomLog.isWarmth());
        entity.setFunctionalAbility(symptomLog.getFunctionalAbility());
        entity.setNotes(symptomLog.getNotes());
        entity.setLogDate(symptomLog.getLogDate());
        entity.setCreatedAt(symptomLog.getCreatedAt());
        return entity;
    }
}
