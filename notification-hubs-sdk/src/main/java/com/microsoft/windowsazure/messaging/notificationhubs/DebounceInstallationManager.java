package com.microsoft.windowsazure.messaging.notificationhubs;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DebounceInstallationManager implements InstallationManager {
    private final InstallationManager mDecorated;
    private final ExecutorService mExecutor;
    private Future<?> mPrevious;
    private long mDebouncePeriod;

    public static final long DEFAULT_DEBOUNCE_PERIOD = 500;

    public DebounceInstallationManager(InstallationManager decorated) {
        this(decorated, Executors.newSingleThreadExecutor(), DEFAULT_DEBOUNCE_PERIOD);
    }

    public DebounceInstallationManager(InstallationManager decorated, ExecutorService executor, long debouncePeriodMillis) {
        mDecorated = decorated;
        mExecutor = executor;
        mDebouncePeriod = debouncePeriodMillis;
    }

    @Override
    public synchronized void saveInstallation(final Installation installation) {
        if(!(mPrevious == null || mPrevious.isDone())) {
            mPrevious.cancel(true);
            try {
                mPrevious.get();
            } catch (InterruptedException e) {
                return;
            } catch (ExecutionException e) {
                return;
            } catch (CancellationException ignored) {
                // Intentionally Left Blank
            }
        }

        mPrevious = mExecutor.submit(() -> {
            try {
                Thread.sleep(mDebouncePeriod);
                mDecorated.saveInstallation(installation);
            } catch (InterruptedException ignored) {
                // Intentionally Left Blank
            }
        });
    }
}
