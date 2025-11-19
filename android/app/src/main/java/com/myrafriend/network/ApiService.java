package com.myrafriend.network;

import com.myrafriend.models.*;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Retrofit API Service Interface
 * Defines all REST API endpoints for My RA Friend
 */
public interface ApiService {

    // ============================================
    // AUTHENTICATION ENDPOINTS
    // ============================================

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/refresh")
    Call<Map<String, String>> refreshToken();

    @POST("auth/update-fcm-token")
    Call<ApiResponse<Void>> updateFcmToken(@Body Map<String, String> fcmToken);

    // ============================================
    // PATIENT ENDPOINTS
    // ============================================

    // Symptom Logs
    @POST("patient/symptom-logs")
    Call<ApiResponse<Void>> submitSymptomLog(@Body SymptomLog symptomLog);

    @GET("patient/symptom-logs/{patient_id}")
    Call<ApiResponse<List<SymptomLog>>> getSymptomHistory(@Path("patient_id") int patientId);

    // Flare Reports
    @POST("patient/flare-report")
    Call<ApiResponse<Void>> submitFlareReport(@Body Map<String, Object> flareReport);

    @POST("patient/flare-report")
    Call<ApiResponse<Void>> reportFlare(@Body Map<String, Object> flareReport);

    // Medications
    @GET("patient/medications/{patient_id}")
    Call<ApiResponse<List<Medication>>> getAssignedMedications(@Path("patient_id") int patientId);

    @POST("patient/medication-intake")
    Call<ApiResponse<Void>> recordMedicationIntake(@Body Map<String, Object> intake);

    @GET("patient/adherence-metrics/{patient_id}")
    Call<ApiResponse<Map<String, Object>>> getAdherenceMetrics(@Path("patient_id") int patientId);

    // Rehabilitation
    @GET("patient/rehab/{patient_id}")
    Call<ApiResponse<List<Map<String, Object>>>> getAssignedRehab(@Path("patient_id") int patientId);

    @POST("patient/rehab-completion")
    Call<ApiResponse<Void>> markRehabComplete(@Body Map<String, Object> completion);

    // Lab Reports
    @Multipart
    @POST("patient/lab-reports")
    Call<ApiResponse<Map<String, String>>> uploadLabReport(
            @Part MultipartBody.Part file,
            @Part("report_type") RequestBody reportType
    );

    @GET("patient/lab-reports/{patient_id}")
    Call<ApiResponse<List<Map<String, Object>>>> getLabReports(@Path("patient_id") int patientId);

    // ============================================
    // DOCTOR ENDPOINTS
    // ============================================

    @GET("doctor/patients/{doctor_id}")
    Call<ApiResponse<List<Map<String, Object>>>> getAssignedPatients(@Path("doctor_id") int doctorId);

    @GET("doctor/patient-detail/{patient_id}")
    Call<ApiResponse<Map<String, Object>>> getPatientDetail(@Path("patient_id") int patientId);

    @POST("doctor/prescribe-medication")
    Call<ApiResponse<Void>> prescribeMedication(@Body Map<String, Object> medicationPlan);

    @POST("doctor/assign-rehab")
    Call<ApiResponse<Void>> assignRehab(@Body Map<String, Object> rehabPlan);

    @POST("doctor/lab-report-interpretation")
    Call<ApiResponse<Void>> addLabReportInterpretation(@Body Map<String, Object> interpretation);

    @GET("doctor/medications-master")
    Call<ApiResponse<List<Map<String, Object>>>> getMedicationsMaster();

    @GET("doctor/rehab-exercises-master")
    Call<ApiResponse<List<Map<String, Object>>>> getRehabExercisesMaster();

    // ============================================
    // MESSAGING ENDPOINTS
    // ============================================

    @GET("messages")
    Call<ApiResponse<List<Map<String, Object>>>> getMessages(@Query("recipient_id") Integer recipientId);

    @POST("messages/send")
    Call<ApiResponse<Void>> sendMessage(@Body Map<String, Object> message);

    @PUT("messages/mark-read")
    Call<ApiResponse<Void>> markMessagesAsRead(@Body Map<String, Object> messageIds);

    // ============================================
    // APPOINTMENT ENDPOINTS
    // ============================================

    @GET("appointments")
    Call<ApiResponse<List<Map<String, Object>>>> getAppointments();

    @POST("appointments/book")
    Call<ApiResponse<Void>> bookAppointment(@Body Map<String, Object> appointment);

    @PUT("appointments/cancel/{appointment_id}")
    Call<ApiResponse<Void>> cancelAppointment(@Path("appointment_id") int appointmentId);
}
