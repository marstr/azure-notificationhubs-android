package com.microsoft.windowsazure.messaging.notificationhubs;

/**
 * InstallationEnricher offers a mechanism for customizing an Installation as it's being created,
 * before it's sent to a {@link InstallationManager} for processing.
 */
public interface InstallationEnricher {
    void enrichInstallation(Installation subject);
}
