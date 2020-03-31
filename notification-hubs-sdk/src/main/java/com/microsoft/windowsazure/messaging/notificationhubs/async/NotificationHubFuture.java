package com.microsoft.windowsazure.messaging.notificationhubs.async;

public interface NotificationHubFuture<T> {

    /**
     * Waits if necessary for the computation to complete, and then
     * retrieves its result.
     *
     * @return the computed result.
     */
    T get();

    /**
     * Execute the consumer once the computation is completed with the result.
     * The consumer function is called in the U.I. thread.
     *
     * @param function the action to perform upon completion.
     */
    void thenAccept(NotificationHubConsumer<T> function);

    boolean isDone();

}