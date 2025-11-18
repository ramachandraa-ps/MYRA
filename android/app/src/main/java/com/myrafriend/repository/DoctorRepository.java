package com.myrafriend.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
 * Doctor Repository
 * Handles doctor-specific operations like patient management, prescribing, etc.
 */
public class DoctorRepository {
    private static final String TAG = "DoctorRepository";
    private final ApiService apiService;
    private final RetrofitClient retrofitClient;

    public DoctorRepository(Context context) {
        retrofitClient = RetrofitClient.getInstance(context);
        apiService = retrofitClient.getApiService();
    }

    /**
     * Get assigned patients
     */
    public LiveData<AuthRepository.Resource<List<Object>>> getAssignedPatients(int doctorId) {
        MutableLiveData<AuthRepository.Resource<List<Object>>> result = new MutableLiveData<>();
        result.setValue(AuthRepository.Resource.loading(null));

        apiService.getAssignedPatients(doctorId).enqueue(new Callback<ApiResponse<List<Object>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Object>>> call,
                                   @NonNull Response<ApiResponse<List<Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Object>> apiResponse = response.body();

                    if (apiResponse.isSuccess()) {
                        result.setValue(AuthRepository.Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(AuthRepository.Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(AuthRepository.Resource.error("Failed to get patients", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Object>>> call, @NonNull Throwable t) {
                Log.e(TAG, "Get patients error: " + t.getMessage());
                result.setValue(AuthRepository.Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Get patient detail
     */
    public LiveData<AuthRepository.Resource<Object>> getPatientDetail(int patientId) {
        MutableLiveData<AuthRepository.Resource<Object>> result = new MutableLiveData<>();
        result.setValue(AuthRepository.Resource.loading(null));

        apiService.getPatientDetail(patientId).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Object>> call,
                                   @NonNull Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Object> apiResponse = response.body();

                    if (apiResponse.isSuccess()) {
                        result.setValue(AuthRepository.Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(AuthRepository.Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(AuthRepository.Resource.error("Failed to get patient detail", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Object>> call, @NonNull Throwable t) {
                Log.e(TAG, "Get patient detail error: " + t.getMessage());
                result.setValue(AuthRepository.Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Prescribe medication to patient
     */
    public LiveData<AuthRepository.Resource<Void>> prescribeMedication(
            int patientId, int medicationId, String dosage, String frequency,
            String timing, int durationWeeks, String instructions) {
        MutableLiveData<AuthRepository.Resource<Void>> result = new MutableLiveData<>();
        result.setValue(AuthRepository.Resource.loading(null));

        Map<String, Object> prescriptionData = new HashMap<>();
        prescriptionData.put("patient_id", patientId);
        prescriptionData.put("medication_id", medicationId);
        prescriptionData.put("dosage", dosage);
        prescriptionData.put("frequency", frequency);
        prescriptionData.put("timing", timing);
        prescriptionData.put("duration_weeks", durationWeeks);
        prescriptionData.put("instructions", instructions);

        apiService.prescribeMedication(prescriptionData).enqueue(new Callback<ApiResponse<Void>>() {
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
                    result.setValue(AuthRepository.Resource.error("Failed to prescribe medication", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                Log.e(TAG, "Prescribe medication error: " + t.getMessage());
                result.setValue(AuthRepository.Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Assign rehab exercise to patient
     */
    public LiveData<AuthRepository.Resource<Void>> assignRehab(
            int patientId, int rehabId, int frequency, int duration, String instructions) {
        MutableLiveData<AuthRepository.Resource<Void>> result = new MutableLiveData<>();
        result.setValue(AuthRepository.Resource.loading(null));

        Map<String, Object> rehabData = new HashMap<>();
        rehabData.put("patient_id", patientId);
        rehabData.put("rehab_id", rehabId);
        rehabData.put("frequency", frequency);
        rehabData.put("duration", duration);
        rehabData.put("instructions", instructions);

        apiService.assignRehab(rehabData).enqueue(new Callback<ApiResponse<Void>>() {
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
                    result.setValue(AuthRepository.Resource.error("Failed to assign rehab", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                Log.e(TAG, "Assign rehab error: " + t.getMessage());
                result.setValue(AuthRepository.Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Get medications master list
     */
    public LiveData<AuthRepository.Resource<List<Object>>> getMedicationsMaster() {
        MutableLiveData<AuthRepository.Resource<List<Object>>> result = new MutableLiveData<>();
        result.setValue(AuthRepository.Resource.loading(null));

        apiService.getMedicationsMaster().enqueue(new Callback<ApiResponse<List<Object>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Object>>> call,
                                   @NonNull Response<ApiResponse<List<Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Object>> apiResponse = response.body();

                    if (apiResponse.isSuccess()) {
                        result.setValue(AuthRepository.Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(AuthRepository.Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(AuthRepository.Resource.error("Failed to get medications", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Object>>> call, @NonNull Throwable t) {
                Log.e(TAG, "Get medications master error: " + t.getMessage());
                result.setValue(AuthRepository.Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Get rehab exercises master list
     */
    public LiveData<AuthRepository.Resource<List<Object>>> getRehabExercisesMaster() {
        MutableLiveData<AuthRepository.Resource<List<Object>>> result = new MutableLiveData<>();
        result.setValue(AuthRepository.Resource.loading(null));

        apiService.getRehabExercisesMaster().enqueue(new Callback<ApiResponse<List<Object>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Object>>> call,
                                   @NonNull Response<ApiResponse<List<Object>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Object>> apiResponse = response.body();

                    if (apiResponse.isSuccess()) {
                        result.setValue(AuthRepository.Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(AuthRepository.Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(AuthRepository.Resource.error("Failed to get rehab exercises", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Object>>> call, @NonNull Throwable t) {
                Log.e(TAG, "Get rehab exercises error: " + t.getMessage());
                result.setValue(AuthRepository.Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }
}
