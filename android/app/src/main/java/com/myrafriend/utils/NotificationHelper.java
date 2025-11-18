package com.myrafriend.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.myrafriend.R;

/**
 * Notification Helper Class
 * Handles local notifications and notification channels
 */
public class NotificationHelper {

    private static final String CHANNEL_ID_MEDICATIONS = "medications_channel";
    private static final String CHANNEL_ID_APPOINTMENTS = "appointments_channel";
    private static final String CHANNEL_ID_MESSAGES = "messages_channel";
    private static final String CHANNEL_ID_FLARES = "flares_channel";

    private static final String CHANNEL_NAME_MEDICATIONS = "Medication Reminders";
    private static final String CHANNEL_NAME_APPOINTMENTS = "Appointments";
    private static final String CHANNEL_NAME_MESSAGES = "Messages";
    private static final String CHANNEL_NAME_FLARES = "Flare Alerts";

    /**
     * Create notification channels (Android O and above)
     */
    public static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager == null) {
                return;
            }

            // Medications channel
            NotificationChannel medicationsChannel = new NotificationChannel(
                    CHANNEL_ID_MEDICATIONS,
                    CHANNEL_NAME_MEDICATIONS,
                    NotificationManager.IMPORTANCE_HIGH
            );
            medicationsChannel.setDescription("Reminders for medication intake");
            notificationManager.createNotificationChannel(medicationsChannel);

            // Appointments channel
            NotificationChannel appointmentsChannel = new NotificationChannel(
                    CHANNEL_ID_APPOINTMENTS,
                    CHANNEL_NAME_APPOINTMENTS,
                    NotificationManager.IMPORTANCE_HIGH
            );
            appointmentsChannel.setDescription("Appointment reminders and updates");
            notificationManager.createNotificationChannel(appointmentsChannel);

            // Messages channel
            NotificationChannel messagesChannel = new NotificationChannel(
                    CHANNEL_ID_MESSAGES,
                    CHANNEL_NAME_MESSAGES,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            messagesChannel.setDescription("New messages from doctors/patients");
            notificationManager.createNotificationChannel(messagesChannel);

            // Flares channel
            NotificationChannel flaresChannel = new NotificationChannel(
                    CHANNEL_ID_FLARES,
                    CHANNEL_NAME_FLARES,
                    NotificationManager.IMPORTANCE_HIGH
            );
            flaresChannel.setDescription("Patient flare alerts");
            notificationManager.createNotificationChannel(flaresChannel);
        }
    }

    /**
     * Show medication reminder notification
     */
    public static void showMedicationReminder(Context context, String medicationName, String dosage) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_MEDICATIONS)
                .setSmallIcon(R.drawable.ic_medication)
                .setContentTitle("Medication Reminder")
                .setContentText("Time to take " + medicationName + " - " + dosage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(generateNotificationId(), builder.build());
        }
    }

    /**
     * Show appointment reminder notification
     */
    public static void showAppointmentReminder(Context context, String appointmentDate, String appointmentTime) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_APPOINTMENTS)
                .setSmallIcon(R.drawable.ic_appointment)
                .setContentTitle("Appointment Reminder")
                .setContentText("You have an appointment on " + appointmentDate + " at " + appointmentTime)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(generateNotificationId(), builder.build());
        }
    }

    /**
     * Show new message notification
     */
    public static void showNewMessageNotification(Context context, String senderName, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_MESSAGES)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("New message from " + senderName)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(generateNotificationId(), builder.build());
        }
    }

    /**
     * Show flare alert notification (for doctors)
     */
    public static void showFlareAlertNotification(Context context, String patientName, String severity) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_FLARES)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle("Flare Alert")
                .setContentText(patientName + " reported a " + severity + " severity flare")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(generateNotificationId(), builder.build());
        }
    }

    /**
     * Show generic notification
     */
    public static void showNotification(Context context, String title, String message, String channelId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(generateNotificationId(), builder.build());
        }
    }

    /**
     * Cancel all notifications
     */
    public static void cancelAllNotifications(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }

    /**
     * Generate unique notification ID
     */
    private static int generateNotificationId() {
        return (int) System.currentTimeMillis();
    }
}
