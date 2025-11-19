package com.myrafriend.ui.fragments.patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.myrafriend.R;
import com.myrafriend.network.RetrofitClient;
import com.myrafriend.viewmodels.MedicationViewModel;

/**
 * Medication List Fragment
 * Shows assigned medications and adherence tracking
 */
public class MedicationListFragment extends Fragment {
    private RecyclerView rvMedications;
    private LinearLayout layoutEmptyState;
    private ProgressBar progressBar;

    private MedicationViewModel medicationViewModel;
    private RetrofitClient retrofitClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medication_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        medicationViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        retrofitClient = RetrofitClient.getInstance(requireContext());

        // Initialize views
        initViews(view);

        // Load medications
        loadMedications();
    }

    /**
     * Initialize views
     */
    private void initViews(View view) {
        rvMedications = view.findViewById(R.id.rv_medications);
        layoutEmptyState = view.findViewById(R.id.layout_empty_state);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    /**
     * Load medications
     */
    private void loadMedications() {
        int patientId = retrofitClient.getUserId();

        medicationViewModel.getAssignedMedications(patientId)
                .observe(getViewLifecycleOwner(), medications -> {
                    progressBar.setVisibility(View.GONE);

                    if (medications != null && !medications.isEmpty()) {
                        layoutEmptyState.setVisibility(View.GONE);
                        rvMedications.setVisibility(View.VISIBLE);
                        // TODO: Set up adapter with medications
                    } else {
                        rvMedications.setVisibility(View.GONE);
                        layoutEmptyState.setVisibility(View.VISIBLE);
                    }
                });
    }
}
