package com.myrafriend.ui.fragments.patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.myrafriend.R;
import com.myrafriend.network.RetrofitClient;
import com.myrafriend.ui.MainActivity;

/**
 * Patient Dashboard Fragment
 * Shows quick actions and recent activity
 */
public class PatientDashboardFragment extends Fragment {
    private TextView tvUserName;
    private MaterialCardView cardLogSymptoms;
    private MaterialCardView cardMedications;
    private MaterialCardView cardRehab;
    private MaterialCardView cardMessages;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        initViews(view);

        // Load user data
        loadUserData();

        // Set up click listeners
        setupClickListeners();
    }

    /**
     * Initialize views
     */
    private void initViews(View view) {
        tvUserName = view.findViewById(R.id.tv_user_name);
        cardLogSymptoms = view.findViewById(R.id.card_log_symptoms);
        cardMedications = view.findViewById(R.id.card_medications);
        cardRehab = view.findViewById(R.id.card_rehab);
        cardMessages = view.findViewById(R.id.card_messages);
    }

    /**
     * Load user data
     */
    private void loadUserData() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance(requireContext());
        String fullName = retrofitClient.getUserFullName();
        if (fullName != null) {
            tvUserName.setText(fullName);
        }
    }

    /**
     * Set up click listeners
     */
    private void setupClickListeners() {
        cardLogSymptoms.setOnClickListener(v -> navigateToSymptoms());
        cardMedications.setOnClickListener(v -> navigateToMedications());
        cardRehab.setOnClickListener(v -> navigateToRehab());
        cardMessages.setOnClickListener(v -> navigateToMessages());
    }

    /**
     * Navigate to symptoms fragment
     */
    private void navigateToSymptoms() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            // Trigger bottom navigation
            View bottomNav = mainActivity.findViewById(R.id.bottom_navigation);
            if (bottomNav instanceof com.google.android.material.bottomnavigation.BottomNavigationView) {
                ((com.google.android.material.bottomnavigation.BottomNavigationView) bottomNav)
                        .setSelectedItemId(R.id.nav_symptoms);
            }
        }
    }

    /**
     * Navigate to medications fragment
     */
    private void navigateToMedications() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            View bottomNav = mainActivity.findViewById(R.id.bottom_navigation);
            if (bottomNav instanceof com.google.android.material.bottomnavigation.BottomNavigationView) {
                ((com.google.android.material.bottomnavigation.BottomNavigationView) bottomNav)
                        .setSelectedItemId(R.id.nav_medications);
            }
        }
    }

    /**
     * Navigate to rehab fragment
     */
    private void navigateToRehab() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            View bottomNav = mainActivity.findViewById(R.id.bottom_navigation);
            if (bottomNav instanceof com.google.android.material.bottomnavigation.BottomNavigationView) {
                ((com.google.android.material.bottomnavigation.BottomNavigationView) bottomNav)
                        .setSelectedItemId(R.id.nav_rehab);
            }
        }
    }

    /**
     * Navigate to messages fragment
     */
    private void navigateToMessages() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            View bottomNav = mainActivity.findViewById(R.id.bottom_navigation);
            if (bottomNav instanceof com.google.android.material.bottomnavigation.BottomNavigationView) {
                ((com.google.android.material.bottomnavigation.BottomNavigationView) bottomNav)
                        .setSelectedItemId(R.id.nav_messages);
            }
        }
    }
}
