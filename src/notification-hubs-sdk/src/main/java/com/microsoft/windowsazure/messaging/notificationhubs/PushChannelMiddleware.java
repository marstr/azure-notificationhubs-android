package com.microsoft.windowsazure.messaging.notificationhubs;

public class PushChannelMiddleware implements InstallationMiddleware {
    @Override
    public InstallationEnricher getInstallationEnricher(InstallationEnricher next) {
        return subject -> {
            // TODO: retrieve the current unique identifier, and apply it to `subject` as the PushChannel
            next.enrichInstallation(subject);
        };
    }
}
