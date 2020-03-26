package com.microsoft.windowsazure.messaging.notificationhubs;

import java.util.Set;

public class TagMiddleware implements InstallationMiddleware {

    private Set<String> tags;

    public boolean addTag(String tag) {
        return tags.add(tag);
    }

    public boolean removeTag(String tag) {
        return tags.remove(tag);
    }

    public Iterable<String> getTags() {
        return tags;
    }

    @Override
    public InstallationEnricher getInstallationEnricher(InstallationEnricher next) {
        return subject -> {
            for (String tag: getTags()) {
                subject.addTag(tag);
            }
            next.enrichInstallation(subject);
        };
    }
}
