package com.myrafriend;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.myrafriend.utils.NotificationHelper;

/**
 * Application class for My RA Friend
 * Initializes app-wide components
 */
public class MyRAFriendApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Create notification channels
        NotificationHelper.createNotificationChannels(this);
    }
}
