package com.myrafriend.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.myrafriend.R;
import com.myrafriend.network.RetrofitClient;
import com.myrafriend.ui.fragments.patient.MedicationListFragment;
import com.myrafriend.ui.fragments.patient.MessagesFragment;
import com.myrafriend.ui.fragments.patient.PatientDashboardFragment;
import com.myrafriend.ui.fragments.patient.RehabListFragment;
import com.myrafriend.ui.fragments.patient.SymptomLogFragment;

/**
 * Main Activity
 * Container for fragments with bottom navigation
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigation;
    private TextView tvToolbarTitle;
    private RetrofitClient retrofitClient;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RetrofitClient
        retrofitClient = RetrofitClient.getInstance(this);
        userRole = retrofitClient.getUserRole();

        // Initialize views
        initViews();

        // Set up bottom navigation
        setupBottomNavigation();

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new PatientDashboardFragment());
            tvToolbarTitle.setText(R.string.nav_dashboard);
        }
    }

    /**
     * Initialize views
     */
    private void initViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
    }

    /**
     * Set up bottom navigation
     */
    private void setupBottomNavigation() {
        // Set appropriate menu based on user role
        if ("patient".equalsIgnoreCase(userRole)) {
            bottomNavigation.inflateMenu(R.menu.bottom_navigation_patient);
        } else if ("doctor".equalsIgnoreCase(userRole)) {
            bottomNavigation.inflateMenu(R.menu.bottom_navigation_doctor);
        }

        // Set navigation item selected listener
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            String title = "";

            int itemId = item.getItemId();

            if (itemId == R.id.nav_dashboard) {
                fragment = new PatientDashboardFragment();
                title = getString(R.string.nav_dashboard);
            } else if (itemId == R.id.nav_symptoms) {
                fragment = new SymptomLogFragment();
                title = getString(R.string.nav_symptoms);
            } else if (itemId == R.id.nav_medications) {
                fragment = new MedicationListFragment();
                title = getString(R.string.nav_medications);
            } else if (itemId == R.id.nav_rehab) {
                fragment = new RehabListFragment();
                title = getString(R.string.nav_rehab);
            } else if (itemId == R.id.nav_messages) {
                fragment = new MessagesFragment();
                title = getString(R.string.nav_messages);
            }

            if (fragment != null) {
                loadFragment(fragment);
                tvToolbarTitle.setText(title);
                return true;
            }

            return false;
        });
    }

    /**
     * Load fragment into container
     */
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    /**
     * Handle back button press
     */
    @Override
    public void onBackPressed() {
        // Show exit confirmation dialog
        new MaterialAlertDialogBuilder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    finishAffinity();
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Logout method (can be called from fragments)
     */
    public void logout() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.logout)
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Clear user data
                    retrofitClient.clearUserData();

                    // Navigate to login
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
