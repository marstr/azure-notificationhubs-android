package com.microsoft.windowsazure.messaging.notificationhubs;

/**
 * A temporary class to facilitate development until the long-term default implementation is completed.
 * That default will be a Manager that calls out to NotificationHub's Installation API.
 */
final class NoopInstallationManager implements InstallationManager {
    // TODO: Delete this class.

    @Override
    public void saveInstallation(Installation installation) {
        // Intentionally Left Blank
    }
}
