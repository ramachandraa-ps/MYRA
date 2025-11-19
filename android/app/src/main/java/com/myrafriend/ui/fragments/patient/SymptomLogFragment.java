package com.myrafriend.ui.fragments.patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.myrafriend.R;
import com.myrafriend.models.SymptomLog;
import com.myrafriend.network.RetrofitClient;
import com.myrafriend.repository.AuthRepository;
import com.myrafriend.utils.DateTimeUtils;
import com.myrafriend.viewmodels.SymptomViewModel;

/**
 * Symptom Log Fragment
 * Allows patients to log their daily symptoms
 */
public class SymptomLogFragment extends Fragment {
    private Slider sliderPainLevel;
    private Slider sliderJointCount;
    private TextInputEditText etMorningStiffness;
    private Slider sliderFatigueLevel;
    private MaterialCheckBox checkboxSwelling;
    private MaterialCheckBox checkboxWarmth;
    private TextInputEditText etFunctionalAbility;
    private TextInputEditText etNotes;
    private MaterialButton btnSubmit;
    private MaterialButton btnReportFlare;
    private ProgressBar progressBar;

    private SymptomViewModel symptomViewModel;
    private RetrofitClient retrofitClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_symptom_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        symptomViewModel = new ViewModelProvider(this).get(SymptomViewModel.class);
        retrofitClient = RetrofitClient.getInstance(requireContext());

        // Initialize views
        initViews(view);

        // Set up click listeners
        setupClickListeners();
    }

    /**
     * Initialize views
     */
    private void initViews(View view) {
        sliderPainLevel = view.findViewById(R.id.slider_pain_level);
        sliderJointCount = view.findViewById(R.id.slider_joint_count);
        etMorningStiffness = view.findViewById(R.id.et_morning_stiffness);
        sliderFatigueLevel = view.findViewById(R.id.slider_fatigue_level);
        checkboxSwelling = view.findViewById(R.id.checkbox_swelling);
        checkboxWarmth = view.findViewById(R.id.checkbox_warmth);
        etFunctionalAbility = view.findViewById(R.id.et_functional_ability);
        etNotes = view.findViewById(R.id.et_notes);
        btnSubmit = view.findViewById(R.id.btn_submit_symptom_log);
        btnReportFlare = view.findViewById(R.id.btn_report_flare);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    /**
     * Set up click listeners
     */
    private void setupClickListeners() {
        btnSubmit.setOnClickListener(v -> submitSymptomLog());
        btnReportFlare.setOnClickListener(v -> reportFlare());
    }

    /**
     * Submit symptom log
     */
    private void submitSymptomLog() {
        // Get values from inputs
        int painLevel = (int) sliderPainLevel.getValue();
        int jointCount = (int) sliderJointCount.getValue();
        String morningStiffness = etMorningStiffness.getText().toString().trim();
        int fatigueLevel = (int) sliderFatigueLevel.getValue();
        boolean swelling = checkboxSwelling.isChecked();
        boolean warmth = checkboxWarmth.isChecked();
        String functionalAbility = etFunctionalAbility.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        // Validate inputs
        if (morningStiffness.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter morning stiffness duration",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (functionalAbility.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter functional ability",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Create symptom log object
        SymptomLog symptomLog = new SymptomLog(
                retrofitClient.getUserId(),
                painLevel,
                jointCount,
                morningStiffness,
                fatigueLevel,
                swelling,
                warmth,
                functionalAbility,
                notes,
                DateTimeUtils.getCurrentDate(),
                DateTimeUtils.getCurrentDateTime()
        );

        // Submit to server
        symptomViewModel.submitSymptomLog(symptomLog).observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) {
                return;
            }

            switch (resource.getStatus()) {
                case LOADING:
                    showLoading(true);
                    break;

                case SUCCESS:
                    showLoading(false);
                    Toast.makeText(requireContext(), R.string.symptom_logged,
                            Toast.LENGTH_SHORT).show();
                    clearForm();
                    break;

                case ERROR:
                    showLoading(false);
                    Toast.makeText(requireContext(),
                            resource.getMessage() != null ? resource.getMessage() :
                                    getString(R.string.error_occurred),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    /**
     * Report flare to doctor
     */
    private void reportFlare() {
        // Show flare severity dialog
        String[] severityOptions = {"Mild", "Moderate", "Severe"};

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Report Flare")
                .setItems(severityOptions, (dialog, which) -> {
                    String severity = severityOptions[which];
                    String notes = etNotes.getText().toString().trim();

                    // Submit flare report
                    symptomViewModel.reportFlare(
                            retrofitClient.getUserId(),
                            severity,
                            notes
                    ).observe(getViewLifecycleOwner(), resource -> {
                        if (resource == null) {
                            return;
                        }

                        switch (resource.getStatus()) {
                            case LOADING:
                                showLoading(true);
                                break;

                            case SUCCESS:
                                showLoading(false);
                                Toast.makeText(requireContext(),
                                        "Flare reported to your doctor",
                                        Toast.LENGTH_SHORT).show();
                                break;

                            case ERROR:
                                showLoading(false);
                                Toast.makeText(requireContext(),
                                        resource.getMessage() != null ? resource.getMessage() :
                                                getString(R.string.error_occurred),
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    });
                })
                .show();
    }

    /**
     * Show/hide loading
     */
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnSubmit.setEnabled(false);
            btnReportFlare.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnSubmit.setEnabled(true);
            btnReportFlare.setEnabled(true);
        }
    }

    /**
     * Clear form
     */
    private void clearForm() {
        sliderPainLevel.setValue(0);
        sliderJointCount.setValue(0);
        etMorningStiffness.setText("");
        sliderFatigueLevel.setValue(0);
        checkboxSwelling.setChecked(false);
        checkboxWarmth.setChecked(false);
        etFunctionalAbility.setText("");
        etNotes.setText("");
    }
}
