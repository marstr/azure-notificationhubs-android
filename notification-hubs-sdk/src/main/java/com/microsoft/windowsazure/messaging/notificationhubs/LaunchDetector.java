package com.microsoft.windowsazure.messaging.notificationhubs;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class LaunchDetector implements Application.ActivityLifecycleCallbacks {
    private static final String EXTRA_GOOGLE_PREFIX = "google.";
    private static final String EXTRA_GOOGLE_TITLE = EXTRA_GOOGLE_PREFIX + "title";
    private static final String EXTRA_GOOGLE_BODY = EXTRA_GOOGLE_PREFIX + "body";
    private static final String EXTRA_GOOGLE_MESSAGE_ID = EXTRA_GOOGLE_PREFIX + "message_id";

    private NotificationHub mHub;

    private static final Set<String> EXTRA_STANDARD_KEYS = new HashSet<String>(){
        {
            add("collapse_key");
            add("from");
        }
    };

    public LaunchDetector(NotificationHub hub) {
        mHub = hub;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // Intentionally Left Blank
    }

    @Override
    public void onActivityStarted(Activity activity) {
        // Intentionally Left Blank
    }

    @Override
    public void onActivityResumed(Activity activity) {
        processLaunch(activity.getIntent());
    }

    @Override
    public void onActivityPaused(Activity activity) {
        // Intentionally Left Blank
    }

    @Override
    public void onActivityStopped(Activity activity) {
        // Intentionally Left Blank
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        // Intentionally Left Blank
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        // Intentionally Left Blank
    }

    /**
     * Detects whether or not the launched application was the result of a notification being
     * clicked in the system tray. If it was, it relays that notification to NotificationHub for
     * further processing.
     *
     * @param i
     */
    public void processLaunch(Intent i) {
        if (i == null || !i.hasCategory(Intent.CATEGORY_LAUNCHER) || !i.hasExtra(EXTRA_GOOGLE_MESSAGE_ID)) {
            return;
        }

        mHub.relayInstanceMessage(getNotificationMessage(i));
    }

    public static NotificationMessage getNotificationMessage(Intent i) {
        Map<String, String> data = new HashMap<String, String>();

        Bundle extras = i.getExtras();
        if (extras == null) {
            return new NotificationMessage(null, null, data);
        }

        for (String key : extras.keySet()) {
            if (isReservedExtra(key)) {
                continue;
            }
            data.put(key, String.valueOf(extras.get(key)));
        }

        String title = extras.getString(EXTRA_GOOGLE_TITLE, null);
        String body = extras.getString(EXTRA_GOOGLE_BODY, null);

        return new NotificationMessage(title, body, data);
    }

    public static boolean isReservedExtra(String key) {
        return key.startsWith(EXTRA_GOOGLE_PREFIX) || EXTRA_STANDARD_KEYS.contains(key);
    }
}
