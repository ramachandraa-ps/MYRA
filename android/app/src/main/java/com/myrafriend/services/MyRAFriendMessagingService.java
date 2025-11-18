package com.myrafriend.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.myrafriend.repository.AuthRepository;
import com.myrafriend.utils.NotificationHelper;

/**
 * Firebase Cloud Messaging Service
 * Handles push notifications and FCM token updates
 */
public class MyRAFriendMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCMService";

    @Override
    public void onCreate() {
        super.onCreate();
        // Create notification channels
        NotificationHelper.createNotificationChannels(this);
    }

    /**
     * Called when a new FCM token is generated
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "New FCM token: " + token);

        // Send token to server
        sendTokenToServer(token);
    }

    /**
     * Called when a push notification is received
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "Message received from: " + remoteMessage.getFrom());

        // Check if message contains data payload
        if (!remoteMessage.getData().isEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            handleDataPayload(remoteMessage.getData());
        }

        // Check if message contains notification payload
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Notification title: " + title);
            Log.d(TAG, "Notification body: " + body);

            // Show notification
            handleNotification(title, body, remoteMessage.getData());
        }
    }

    /**
     * Handle data payload
     */
    private void handleDataPayload(java.util.Map<String, String> data) {
        String notificationType = data.get("notification_type");

        if (notificationType == null) {
            return;
        }

        switch (notificationType) {
            case "medication_reminder":
                handleMedicationReminder(data);
                break;
            case "missed_dose":
                handleMissedDose(data);
                break;
            case "flare_alert":
                handleFlareAlert(data);
                break;
            case "new_message":
                handleNewMessage(data);
                break;
            case "appointment_reminder":
                handleAppointmentReminder(data);
                break;
            case "medication_assigned":
                handleMedicationAssigned(data);
                break;
            case "rehab_assigned":
                handleRehabAssigned(data);
                break;
            default:
                handleGenericNotification(data);
                break;
        }
    }

    /**
     * Handle notification payload
     */
    private void handleNotification(String title, String body, java.util.Map<String, String> data) {
        String notificationType = data.get("notification_type");
        String channelId = getChannelIdForType(notificationType);

        NotificationHelper.showNotification(this, title, body, channelId);
    }

    /**
     * Handle medication reminder
     */
    private void handleMedicationReminder(java.util.Map<String, String> data) {
        String medicationName = data.get("medication_name");
        String dosage = data.get("dosage");

        if (medicationName != null && dosage != null) {
            NotificationHelper.showMedicationReminder(this, medicationName, dosage);
        }
    }

    /**
     * Handle missed dose alert
     */
    private void handleMissedDose(java.util.Map<String, String> data) {
        String medicationName = data.get("medication_name");
        String message = data.get("message");

        if (medicationName != null) {
            NotificationHelper.showNotification(
                this,
                "Missed Dose Alert",
                message != null ? message : "You missed your " + medicationName + " dose",
                "medications_channel"
            );
        }
    }

    /**
     * Handle flare alert (for doctors)
     */
    private void handleFlareAlert(java.util.Map<String, String> data) {
        String patientName = data.get("patient_name");
        String severity = data.get("severity");

        if (patientName != null && severity != null) {
            NotificationHelper.showFlareAlertNotification(this, patientName, severity);
        }
    }

    /**
     * Handle new message
     */
    private void handleNewMessage(java.util.Map<String, String> data) {
        String senderName = data.get("sender_name");
        String message = data.get("message");

        if (senderName != null && message != null) {
            NotificationHelper.showNewMessageNotification(this, senderName, message);
        }
    }

    /**
     * Handle appointment reminder
     */
    private void handleAppointmentReminder(java.util.Map<String, String> data) {
        String appointmentDate = data.get("appointment_date");
        String appointmentTime = data.get("appointment_time");

        if (appointmentDate != null && appointmentTime != null) {
            NotificationHelper.showAppointmentReminder(this, appointmentDate, appointmentTime);
        }
    }

    /**
     * Handle medication assigned
     */
    private void handleMedicationAssigned(java.util.Map<String, String> data) {
        String medicationName = data.get("medication_name");
        String doctorName = data.get("doctor_name");

        if (medicationName != null) {
            NotificationHelper.showNotification(
                this,
                "New Medication Prescribed",
                (doctorName != null ? doctorName : "Your doctor") +
                " prescribed " + medicationName,
                "medications_channel"
            );
        }
    }

    /**
     * Handle rehab assigned
     */
    private void handleRehabAssigned(java.util.Map<String, String> data) {
        String rehabName = data.get("rehab_name");
        String doctorName = data.get("doctor_name");

        if (rehabName != null) {
            NotificationHelper.showNotification(
                this,
                "New Rehab Exercise Assigned",
                (doctorName != null ? doctorName : "Your doctor") +
                " assigned " + rehabName,
                "medications_channel"
            );
        }
    }

    /**
     * Handle generic notification
     */
    private void handleGenericNotification(java.util.Map<String, String> data) {
        String title = data.get("title");
        String message = data.get("message");

        if (title != null && message != null) {
            NotificationHelper.showNotification(
                this,
                title,
                message,
                "messages_channel"
            );
        }
    }

    /**
     * Get notification channel ID based on notification type
     */
    private String getChannelIdForType(String notificationType) {
        if (notificationType == null) {
            return "messages_channel";
        }

        switch (notificationType) {
            case "medication_reminder":
            case "missed_dose":
            case "medication_assigned":
            case "rehab_assigned":
                return "medications_channel";
            case "appointment_reminder":
                return "appointments_channel";
            case "flare_alert":
                return "flares_channel";
            case "new_message":
            default:
                return "messages_channel";
        }
    }

    /**
     * Send FCM token to server
     */
    private void sendTokenToServer(String token) {
        AuthRepository authRepository = new AuthRepository(this);
        authRepository.updateFcmToken(token);
    }
}
