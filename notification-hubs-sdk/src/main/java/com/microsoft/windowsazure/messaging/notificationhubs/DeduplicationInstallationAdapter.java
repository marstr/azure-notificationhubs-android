package com.microsoft.windowsazure.messaging.notificationhubs;

import android.content.Context;
import android.content.SharedPreferences;

import com.microsoft.windowsazure.messaging.R;

import java.util.Date;

/**
 * DeduplicationInstallationAdapter limits the frequency for which two identical {@link Installation}
 * instances can be sent to the backend.
 */
public class DeduplicationInstallationAdapter implements InstallationAdapter {
    static final String LAST_ACCEPTED_HASH_KEY = "lastAcceptedHash";
    static final String LAST_ACCEPTED_TIMESTAMP_KEY= "lastAcceptedTimestamp";
    private static final long DEFAULT_INSTALLATION_STALE_MILLIS = 1000L * 60L * 60L * 24L; // One day's worth of milliseconds

    private SharedPreferences mPreferences;
    private long mInstallationStaleMillis;
    private final InstallationAdapter mDecoratedAdapter;

    public DeduplicationInstallationAdapter(Context context, InstallationAdapter decorated) {
        this(context, decorated, DEFAULT_INSTALLATION_STALE_MILLIS);
    }

    public DeduplicationInstallationAdapter(Context context, InstallationAdapter decorated, long installationStaleMillis) {
        mPreferences = context.getSharedPreferences(context.getString(R.string.installation_enrichment_file_key), Context.MODE_MULTI_PROCESS);
        mInstallationStaleMillis = installationStaleMillis;
        mDecoratedAdapter = decorated;
    }

    /**
     * Sets the maximum amount of time that this instance will wait before allowing what would have
     * otherwise been a duplicate {@link Installation} through.
     * @param millis The number of milliseconds before an {@link Installation} should be considered stale.
     */
    public void setInstallationStaleWindow(long millis) {
        mInstallationStaleMillis = millis;
    }

    /**
     * Updates a backend with the updated Installation information for this device.
     *
     * @param installation            The record to update.
     * @param onInstallationSaved
     * @param onInstallationSaveError
     */
    @Override
    public void saveInstallation(final Installation installation, final Listener onInstallationSaved, final ErrorListener onInstallationSaveError) {
        final int currentHash = installation.hashCode();
        int recentHash = getLastAcceptedHash();

        boolean sameAsLastAccepted = recentHash != 0 && recentHash == currentHash;
        final long currentTime = new Date().getTime();
        boolean lastAcceptedIsRecent =  currentTime < getLastAcceptedTimestamp() + mInstallationStaleMillis;

        if (sameAsLastAccepted && lastAcceptedIsRecent) {
            return;
        }

        Listener updateLastAccepted = new Listener() {
            @Override
            public void onInstallationSaved(Installation i) {
                synchronized (DeduplicationInstallationAdapter.this) {
                    mPreferences.edit().putInt(LAST_ACCEPTED_HASH_KEY, currentHash).apply();
                    mPreferences.edit().putLong(LAST_ACCEPTED_TIMESTAMP_KEY, currentTime).apply();
                }
                onInstallationSaved.onInstallationSaved(i);
            }
        };

        mDecoratedAdapter.saveInstallation(installation, updateLastAccepted, onInstallationSaveError);
    }

    long getLastAcceptedTimestamp() {
        synchronized (DeduplicationInstallationAdapter.this) {
            return mPreferences.getLong(LAST_ACCEPTED_TIMESTAMP_KEY, Long.MIN_VALUE);
        }
    }

    int getLastAcceptedHash() {
        synchronized (DeduplicationInstallationAdapter.this) {
            return mPreferences.getInt(LAST_ACCEPTED_HASH_KEY, 0);
        }
    }
}
