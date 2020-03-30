package com.microsoft.windowsazure.messaging.notificationhubs;

/**
 * Allows for a
 */
final class NoopMiddleware implements InstallationMiddleware {

    @Override
    public InstallationEnricher getInstallationEnricher(InstallationEnricher next) {
        return subject -> {
            // Intentionally Left Blank
        };
    }
}
