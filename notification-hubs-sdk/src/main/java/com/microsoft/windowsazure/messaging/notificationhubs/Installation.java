package com.microsoft.windowsazure.messaging.notificationhubs;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Installation {
    private String installationId;
    private ZonedDateTime expirationTime;
    private Set<String> tags;
    private String platform;
    private String pushChannel;
    private Map<String, InstallationTemplate> templates;

    public Installation() {
        tags = new HashSet<String>();
        templates = new HashMap<String, InstallationTemplate>();
    }

    /**
     * Asserts that this installation has a particular tag.
     * @param tag The tag that should be added to this {@link Installation}.
     * @return True if the tag had not been present, false if it was already present.
     */
    public boolean addTag(String tag) {
        return tags.add(tag);
    }

    /**
     * Adds all of the tags provided, instead of requiring them to be added individually.
     * @param tags The set of tags to be added.
     * @return True if any of the tags in the set provided were added.
     *
     * @implNote Duplicates provided will be ignored.
     */
    public boolean addTags(Collection<? extends String> tags) {
        return this.tags.addAll(tags);
    }

    public boolean removeTag(String tag) {
        return tags.remove(tag);
    }

    public boolean removeTags(Collection<?> tags) {
        return this.tags.removeAll(tags);
    }

    public void clearTags() {
        this.tags.clear();
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

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPushChannel() {
        return pushChannel;
    }

    public void setPushChannel(String pushChannel) {
        this.pushChannel = pushChannel;
    }
}