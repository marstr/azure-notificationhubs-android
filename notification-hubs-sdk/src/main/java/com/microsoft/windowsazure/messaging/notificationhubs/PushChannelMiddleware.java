package com.microsoft.windowsazure.messaging.notificationhubs;

public class PushChannelMiddleware implements InstallationMiddleware {

    private String currentToken;


    @Override
    public synchronized InstallationEnricher getInstallationEnricher(InstallationEnricher next) {
        return subject -> {
            subject.setPushChannel(currentToken);
            next.enrichInstallation(subject);
        };
    }

    public synchronized String getCurrentToken() {
        // TODO: Retrieve token from SharedPreferences
        return currentToken;
    }

    public synchronized void setCurrentToken(String currentToken) {
        // TODO: Store token in SharedPreferences
        this.currentToken = currentToken;
    }
}
