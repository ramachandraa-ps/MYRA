package com.myrafriend.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.myrafriend.network.ApiService;
import com.myrafriend.network.LoginRequest;
import com.myrafriend.network.LoginResponse;
import com.myrafriend.network.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Auth Repository
 * Handles authentication operations
 */
public class AuthRepository {
    private static final String TAG = "AuthRepository";
    private final ApiService apiService;
    private final RetrofitClient retrofitClient;

    public AuthRepository(Context context) {
        retrofitClient = RetrofitClient.getInstance(context);
        apiService = retrofitClient.getApiService();
    }

    /**
     * Login user
     */
    public LiveData<Resource<LoginResponse>> login(String email, String password) {
        MutableLiveData<Resource<LoginResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call,
                                   @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess()) {
                        // Save token and user data
                        retrofitClient.saveToken(loginResponse.getToken());
                        retrofitClient.saveUserData(
                                loginResponse.getUser().getId(),
                                loginResponse.getUser().getRoleId(),
                                loginResponse.getUser().getRole(),
                                loginResponse.getUser().getFullName(),
                                loginResponse.getUser().getEmail()
                        );

                        result.setValue(Resource.success(loginResponse));
                    } else {
                        result.setValue(Resource.error(loginResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Login failed: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Login error: " + t.getMessage());
                result.setValue(Resource.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Update FCM token
     */
    public void updateFcmToken(String fcmToken) {
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("fcm_token", fcmToken);

        apiService.updateFcmToken(tokenMap).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "FCM token updated successfully");
                } else {
                    Log.e(TAG, "Failed to update FCM token");
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e(TAG, "FCM token update error: " + t.getMessage());
            }
        });
    }

    /**
     * Logout user
     */
    public void logout() {
        retrofitClient.clearUserData();
    }

    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return retrofitClient.isLoggedIn();
    }

    /**
     * Get user role
     */
    public String getUserRole() {
        return retrofitClient.getUserRole();
    }

    /**
     * Resource wrapper class for API responses
     */
    public static class Resource<T> {
        public enum Status {
            SUCCESS, ERROR, LOADING
        }

        private final Status status;
        private final T data;
        private final String message;

        private Resource(Status status, T data, String message) {
            this.status = status;
            this.data = data;
            this.message = message;
        }

        public static <T> Resource<T> success(T data) {
            return new Resource<>(Status.SUCCESS, data, null);
        }

        public static <T> Resource<T> error(String message, T data) {
            return new Resource<>(Status.ERROR, data, message);
        }

        public static <T> Resource<T> loading(T data) {
            return new Resource<>(Status.LOADING, data, null);
        }

        public Status getStatus() {
            return status;
        }

        public T getData() {
            return data;
        }

        public String getMessage() {
            return message;
        }
    }
}
