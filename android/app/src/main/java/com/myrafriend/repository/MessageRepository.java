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

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Message Repository
 * Handles messaging and appointment operations
 */
public class MessageRepository {
    private static final String TAG = "MessageRepository";
    private final ApiService apiService;
    private final RetrofitClient retrofitClient;

    public MessageRepository(Context context) {
        retrofitClient = RetrofitClient.getInstance(context);
        apiService = retrofitClient.getApiService();
    }

    /**
     * Get messages
     */
    public LiveData<AuthRepository.Resource<List<Object>>> getMessages(int userId) {
        MutableLiveData<AuthRepository.Resource<List<Object>>> result = new MutableLiveData<>();
        result.setValue(AuthRepository.Resource.loading(null));

        apiService.getMessages(userId).enqueue(new Callback<ApiResponse<List<Object>>>() {
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
                    result.setValue(AuthRepository.Resource.error("Failed to get messages", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Object>>> call, @NonNull Throwable t) {
                Log.e(TAG, "Get messages error: " + t.getMessage());
                result.setValue(AuthRepository.Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Send message
     */
    public LiveData<AuthRepository.Resource<Void>> sendMessage(
            int receiverId, String message, MultipartBody.Part attachment) {
        MutableLiveData<AuthRepository.Resource<Void>> result = new MutableLiveData<>();
        result.setValue(AuthRepository.Resource.loading(null));

        RequestBody receiverIdBody = RequestBody.create(
                okhttp3.MediaType.parse("text/plain"), String.valueOf(receiverId));
        RequestBody messageBody = RequestBody.create(
                okhttp3.MediaType.parse("text/plain"), message);

        apiService.sendMessage(receiverIdBody, messageBody, attachment)
                .enqueue(new Callback<ApiResponse<Void>>() {
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
                    result.setValue(AuthRepository.Resource.error("Failed to send message", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                Log.e(TAG, "Send message error: " + t.getMessage());
                result.setValue(AuthRepository.Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Mark messages as read
     */
    public LiveData<AuthRepository.Resource<Void>> markMessagesAsRead(int userId) {
        MutableLiveData<AuthRepository.Resource<Void>> result = new MutableLiveData<>();
        result.setValue(AuthRepository.Resource.loading(null));

        Map<String, Integer> data = new HashMap<>();
        data.put("user_id", userId);

        apiService.markMessagesAsRead(data).enqueue(new Callback<ApiResponse<Void>>() {
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
                    result.setValue(AuthRepository.Resource.error("Failed to mark messages as read", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                Log.e(TAG, "Mark messages as read error: " + t.getMessage());
                result.setValue(AuthRepository.Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Get appointments
     */
    public LiveData<AuthRepository.Resource<List<Object>>> getAppointments(int userId, String roleId) {
        MutableLiveData<AuthRepository.Resource<List<Object>>> result = new MutableLiveData<>();
        result.setValue(AuthRepository.Resource.loading(null));

        apiService.getAppointments(userId, roleId).enqueue(new Callback<ApiResponse<List<Object>>>() {
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
                    result.setValue(AuthRepository.Resource.error("Failed to get appointments", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Object>>> call, @NonNull Throwable t) {
                Log.e(TAG, "Get appointments error: " + t.getMessage());
                result.setValue(AuthRepository.Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Book appointment
     */
    public LiveData<AuthRepository.Resource<Void>> bookAppointment(
            int doctorId, String appointmentDate, String appointmentTime, String reason) {
        MutableLiveData<AuthRepository.Resource<Void>> result = new MutableLiveData<>();
        result.setValue(AuthRepository.Resource.loading(null));

        Map<String, Object> appointmentData = new HashMap<>();
        appointmentData.put("doctor_id", doctorId);
        appointmentData.put("appointment_date", appointmentDate);
        appointmentData.put("appointment_time", appointmentTime);
        appointmentData.put("reason", reason);

        apiService.bookAppointment(appointmentData).enqueue(new Callback<ApiResponse<Void>>() {
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
                    result.setValue(AuthRepository.Resource.error("Failed to book appointment", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                Log.e(TAG, "Book appointment error: " + t.getMessage());
                result.setValue(AuthRepository.Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Cancel appointment
     */
    public LiveData<AuthRepository.Resource<Void>> cancelAppointment(int appointmentId) {
        MutableLiveData<AuthRepository.Resource<Void>> result = new MutableLiveData<>();
        result.setValue(AuthRepository.Resource.loading(null));

        apiService.cancelAppointment(appointmentId).enqueue(new Callback<ApiResponse<Void>>() {
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
                    result.setValue(AuthRepository.Resource.error("Failed to cancel appointment", null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Void>> call, @NonNull Throwable t) {
                Log.e(TAG, "Cancel appointment error: " + t.getMessage());
                result.setValue(AuthRepository.Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }
}
