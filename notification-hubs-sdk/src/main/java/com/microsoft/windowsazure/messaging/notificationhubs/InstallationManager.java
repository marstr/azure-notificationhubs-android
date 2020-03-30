package com.microsoft.windowsazure.messaging.notificationhubs;

/**
 * An InstallationManager is tasked with processing a fully enriched {@link Installation}.
 *
 * A typical implementation may call a web-service to alert it to changes on a specific device.
 *
 * @see InstallationEnricher
 */
public interface InstallationManager {
    void saveInstallation(Installation installation);
}
