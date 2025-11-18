package com.myrafriend.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.myrafriend.network.LoginResponse;
import com.myrafriend.repository.AuthRepository;

/**
 * Login ViewModel
 * Manages UI state for login screen
 */
public class LoginViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    /**
     * Login with email and password
     */
    public LiveData<AuthRepository.Resource<LoginResponse>> login(String email, String password) {
        return authRepository.login(email, password);
    }

    /**
     * Check if user is already logged in
     */
    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }

    /**
     * Get user role
     */
    public String getUserRole() {
        return authRepository.getUserRole();
    }
}
