package com.microsoft.windowsazure.messaging.notificationhubs;

public class TemplateMiddleware implements InstallationMiddleware {
    @Override
    public InstallationEnricher getInstallationEnricher(InstallationEnricher next) {
        return null;
    }
}
