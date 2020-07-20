package com.microsoft.windowsazure.messaging.notificationhubs;

import android.content.Context;
import android.content.SharedPreferences;

import com.microsoft.windowsazure.messaging.R;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Protects the {@link InstallationAdapter} from rapid changes to the current Installation.
 */
public class DebounceInstallationAdapter implements InstallationAdapter {

    private static final long DEFAULT_DEBOUNCE_INTERVAL = 2000L; // Two seconds

    private final ScheduledExecutorService mScheduler = Executors.newScheduledThreadPool(1);
    private InstallationAdapter mInstallationAdapter;
    private long mInterval;
    private ScheduledFuture<?> mSchedFuture;

    /**
     * Creates a new instance which decorates a given {@link InstallationAdapter} with all default
     * settings.
     * @param context The Application context that can be used for caching information about
     *                previous sessions.
     * @param installationAdapter The adapter that should be invoked once after the waiting period
     *                            is complete.
     */
    public DebounceInstallationAdapter(Context context, InstallationAdapter installationAdapter) {
        this(context, installationAdapter, DEFAULT_DEBOUNCE_INTERVAL);
    }

    /**
     * Creates a new instance which decorates a given {@link InstallationAdapter}, and waits at least
     * a specified number of milliseconds between calls to the server.
     *
     * @param context The Application context that can be used for caching information about
     *                previous sessions.
     * @param installationAdapter The adapter that should be invoked once after the waiting period
     *                            is complete.
     * @param interval The number of milliseconds to wait for further changes to accumulate before
     *                 passing the {@link Installation} to the next adapter.
     */
    public DebounceInstallationAdapter(Context context, InstallationAdapter installationAdapter, long interval) {
        super();
        mInstallationAdapter = installationAdapter;
        mInterval = interval;
    }


    @Override
    public void saveInstallation(final Installation installation, final Listener onInstallationSaved, final ErrorListener onInstallationSaveError) {
        if (mSchedFuture != null && !mSchedFuture.isDone()) {
            mSchedFuture.cancel(true);
        }

        mSchedFuture = mScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    mInstallationAdapter.saveInstallation(installation, onInstallationSaved, onInstallationSaveError);
                    mPreferences.edit().putInt(LAST_ACCEPTED_HASH_KEY, currentHash).apply();
                    mPreferences.edit().putLong(LAST_ACCEPTED_TIMESTAMP_KEY, currentTime).apply();
                } catch (Exception e) {
                    onInstallationSaveError.onInstallationSaveError(e);
                }
            }
        }, mInterval, TimeUnit.MILLISECONDS);
    }

}