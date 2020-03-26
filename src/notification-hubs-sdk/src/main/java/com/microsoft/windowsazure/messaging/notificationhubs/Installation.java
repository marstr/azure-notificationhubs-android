package com.microsoft.windowsazure.messaging.notificationhubs;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;

public class Installation {
    private String installationId;
    private ZonedDateTime expirationTime;
    private Set<String> tags;
    private String platform;
    private String pushChannel;
    private Map<String, InstallationTemplate> templates;

    /**
     * Asserts that this installation has a particular tag.
     * @param tag The tag that should be added to this {@link Installation}.
     * @return True if the tag had not been present, false if it was already present.
     */
    public boolean addTag(String tag) {
        return tags.add(tag);
    }

    public boolean removeTag(String tag) {
        return tags.remove(tag);
    }

    public Iterable<String> getTags() {
        return tags;
    }

    public InstallationTemplate getTemplate(String name) {
        return templates.get(name);
    }

    public void addTemplate(String name, InstallationTemplate subject) {
        templates.put(name, subject);
    }
}