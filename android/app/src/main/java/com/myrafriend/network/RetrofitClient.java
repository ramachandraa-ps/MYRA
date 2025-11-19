package com.myrafriend.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.myrafriend.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit Client Singleton
 * Handles HTTP client configuration and API service creation
 */
public class RetrofitClient {
    private static RetrofitClient instance;
    private final Retrofit retrofit;
    private final ApiService apiService;
    private final Context context;

    private RetrofitClient(Context context) {
        this.context = context.getApplicationContext();

        // Logging interceptor for debugging
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        // Auth interceptor to add JWT token to requests
        Interceptor authInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Get token from SharedPreferences
                SharedPreferences prefs = RetrofitClient.this.context
                        .getSharedPreferences("MyRAFriend", Context.MODE_PRIVATE);
                String token = prefs.getString("jwt_token", null);

                Request.Builder requestBuilder = original.newBuilder();

                // Add token if available
                if (token != null && !token.isEmpty()) {
                    requestBuilder.header("Authorization", "Bearer " + token);
                }

                requestBuilder.header("Accept", "application/json");
                Request request = requestBuilder.build();

                return chain.proceed(request);
            }
        };

        // Build OkHttp client
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        // Build Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static synchronized RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context);
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }

    /**
     * Save JWT token to SharedPreferences
     */
    public void saveToken(String token) {
        SharedPreferences prefs = context.getSharedPreferences("MyRAFriend", Context.MODE_PRIVATE);
        prefs.edit().putString("jwt_token", token).apply();
    }

    /**
     * Get JWT token from SharedPreferences
     */
    public String getToken() {
        SharedPreferences prefs = context.getSharedPreferences("MyRAFriend", Context.MODE_PRIVATE);
        return prefs.getString("jwt_token", null);
    }

    /**
     * Clear JWT token (for logout)
     */
    public void clearToken() {
        SharedPreferences prefs = context.getSharedPreferences("MyRAFriend", Context.MODE_PRIVATE);
        prefs.edit().remove("jwt_token").apply();
    }

    /**
     * Save user data
     */
    public void saveUserData(int userId, int roleId, String role, String fullName, String email) {
        SharedPreferences prefs = context.getSharedPreferences("MyRAFriend", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("user_id", userId);
        editor.putInt("role_id", roleId);
        editor.putString("user_role", role);
        editor.putString("user_name", fullName);
        editor.putString("user_email", email);
        editor.apply();
    }

    /**
     * Get user ID
     */
    public int getUserId() {
        SharedPreferences prefs = context.getSharedPreferences("MyRAFriend", Context.MODE_PRIVATE);
        return prefs.getInt("user_id", -1);
    }

    /**
     * Get role ID (patient_id or doctor_id)
     */
    public int getRoleId() {
        SharedPreferences prefs = context.getSharedPreferences("MyRAFriend", Context.MODE_PRIVATE);
        return prefs.getInt("role_id", -1);
    }

    /**
     * Get user role
     */
    public String getUserRole() {
        SharedPreferences prefs = context.getSharedPreferences("MyRAFriend", Context.MODE_PRIVATE);
        return prefs.getString("user_role", "");
    }

    /**
     * Get user full name
     */
    public String getUserFullName() {
        SharedPreferences prefs = context.getSharedPreferences("MyRAFriend", Context.MODE_PRIVATE);
        return prefs.getString("user_name", "");
    }

    /**
     * Get user email
     */
    public String getUserEmail() {
        SharedPreferences prefs = context.getSharedPreferences("MyRAFriend", Context.MODE_PRIVATE);
        return prefs.getString("user_email", "");
    }

    /**
     * Clear all user data (for logout)
     */
    public void clearUserData() {
        SharedPreferences prefs = context.getSharedPreferences("MyRAFriend", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return getToken() != null && getUserId() != -1;
    }
}
