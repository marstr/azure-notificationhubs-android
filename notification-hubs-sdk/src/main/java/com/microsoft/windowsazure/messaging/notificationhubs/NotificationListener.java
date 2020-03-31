package com.microsoft.windowsazure.messaging.notificationhubs;

import android.app.Activity;

import com.google.firebase.messaging.RemoteMessage;

public interface NotificationListener {
    void onNotificationReceived(Activity activity, RemoteMessage message);
}
