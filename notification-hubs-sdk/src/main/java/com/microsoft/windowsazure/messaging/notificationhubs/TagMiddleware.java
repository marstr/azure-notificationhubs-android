package com.microsoft.windowsazure.messaging.notificationhubs;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Applies a set of tags stored in memory by this class to any {@link Installation} that is to be
 * enriched by this class.
 */
public class TagMiddleware implements InstallationMiddleware {

    private Set<String> tags;

    public boolean addTag(String tag) {
        return tags.add(tag);
    }

    public boolean addTags(Collection<? extends String> tags){
        return this.tags.addAll(tags);
    }

    public TagMiddleware(){
        tags = new HashSet<String>();
    }

    /**
     * Removes a tag from the set that will be applied to future {@link Installation} enrichments.
     * @param tag
     * @return
     */
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

    @Override
    public InstallationEnricher getInstallationEnricher(InstallationEnricher next) {
        return subject -> {
            for (String tag: getTags()) {
                subject.addTag(tag);
            }
            if (next != null) {
                next.enrichInstallation(subject);
            }
        };
    }
}
