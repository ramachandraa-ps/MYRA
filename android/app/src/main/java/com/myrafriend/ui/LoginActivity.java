package com.myrafriend.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.myrafriend.R;
import com.myrafriend.repository.AuthRepository;
import com.myrafriend.viewmodels.LoginViewModel;

/**
 * Login Activity
 * Handles user authentication and navigation to main app
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private LoginViewModel loginViewModel;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private MaterialButton btnLogin;
    private ProgressBar progressBar;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is already logged in
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        if (loginViewModel.isLoggedIn()) {
            navigateToMain();
            return;
        }

        setContentView(R.layout.activity_login);

        // Initialize views
        initViews();

        // Set up click listeners
        setupClickListeners();
    }

    /**
     * Initialize view references
     */
    private void initViews() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progress_bar);
        tvError = findViewById(R.id.tv_error);
    }

    /**
     * Set up click listeners
     */
    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    /**
     * Attempt to login with entered credentials
     */
    private void attemptLogin() {
        // Clear previous error
        tvError.setVisibility(View.GONE);

        // Get email and password
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(email, password)) {
            return;
        }

        // Perform login
        performLogin(email, password);
    }

    /**
     * Validate email and password inputs
     */
    private boolean validateInputs(String email, String password) {
        // Check email
        if (TextUtils.isEmpty(email)) {
            showError("Email is required");
            etEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Invalid email format");
            etEmail.requestFocus();
            return false;
        }

        // Check password
        if (TextUtils.isEmpty(password)) {
            showError("Password is required");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Perform login API call
     */
    private void performLogin(String email, String password) {
        loginViewModel.login(email, password).observe(this, resource -> {
            if (resource == null) {
                return;
            }

            switch (resource.getStatus()) {
                case LOADING:
                    showLoading(true);
                    break;

                case SUCCESS:
                    showLoading(false);
                    if (resource.getData() != null && resource.getData().isSuccess()) {
                        Toast.makeText(this, getString(R.string.login_success),
                                Toast.LENGTH_SHORT).show();
                        navigateToMain();
                    } else {
                        String errorMsg = resource.getData() != null ?
                                resource.getData().getMessage() :
                                getString(R.string.login_error);
                        showError(errorMsg);
                    }
                    break;

                case ERROR:
                    showLoading(false);
                    String errorMessage = resource.getMessage();
                    if (errorMessage != null && errorMessage.contains("Network")) {
                        showError(getString(R.string.network_error));
                    } else {
                        showError(resource.getMessage() != null ?
                                resource.getMessage() :
                                getString(R.string.login_error));
                    }
                    break;
            }
        });
    }

    /**
     * Show/hide loading state
     */
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            btnLogin.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            etEmail.setEnabled(false);
            etPassword.setEnabled(false);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            etEmail.setEnabled(true);
            etPassword.setEnabled(true);
        }
    }

    /**
     * Show error message
     */
    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }

    /**
     * Navigate to main activity
     */
    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // Disable back button on login screen
        // User must login to proceed
    }
}
