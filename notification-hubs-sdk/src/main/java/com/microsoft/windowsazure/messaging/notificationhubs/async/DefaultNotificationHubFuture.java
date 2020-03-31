package com.microsoft.windowsazure.messaging.notificationhubs.async;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of {@link NotificationHubFuture}.
 *
 * @param <T> result type.
 */
public class DefaultNotificationHubFuture<T> implements NotificationHubFuture<T> {

    /**
     * Lock used to wait or monitor result.
     */
    private final CountDownLatch mLatch = new CountDownLatch(1);

    /**
     * Result.
     */
    private T mResult;

    /**
     * Callbacks from thenAccept waiting for result.
     */
    private Collection<NotificationHubConsumer<T>> mConsumers;

    @Override
    public T get() {
        while (true) {
            try {
                mLatch.await();
                break;
            } catch (InterruptedException ignored) {
            }
        }
        return mResult;
    }

    @Override
    public synchronized void thenAccept(final NotificationHubConsumer<T> function) {
        if (isDone()) {
            HandlerExtensions.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    function.accept(mResult);
                }
            });
        } else {
            if (mConsumers == null) {
                mConsumers = new LinkedList<>();
            }
            mConsumers.add(function);
        }
    }

    @Override
    public boolean isDone() {
        return false;
    }

    /**
     * Set result.
     *
     * @param value result.
     */
    public synchronized void complete(final T value) {
        if (!isDone()) {
            mResult = value;
            mLatch.countDown();
            if (mConsumers != null) {
                HandlerExtensions.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        for (NotificationHubConsumer<T> function : mConsumers) {
                            function.accept(value);
                        }
                        mConsumers = null;
                    }
                });
            }
        }
    }

}