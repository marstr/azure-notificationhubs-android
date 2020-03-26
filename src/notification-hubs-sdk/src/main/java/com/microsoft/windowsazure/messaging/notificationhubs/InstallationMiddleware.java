package com.microsoft.windowsazure.messaging.notificationhubs;

public interface InstallationMiddleware {
    InstallationEnricher getInstallationEnricher(InstallationEnricher next);
}
