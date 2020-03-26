package com.microsoft.windowsazure.messaging.notificationhubs;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InstallationTemplate {
    private String body;
    private Map<String, String> headers;
    private Set<String> tags;

    public InstallationTemplate() {
        headers = new HashMap<String, String>();
        tags = new HashSet<String>();
    }

    /**
     * Adds or updates a header associated with this template.
     * @param header The name of the header to be upserted.
     * @param value The value to associate with this header.
     */
    public void setHeader(String header, String value) {
        headers.put(header, value);
    }

    /**
     * Removes a header from this template.
     * @param header The name of the header to be removed.
     * @return True if the set of headers was impacted by this operation.
     */
    public boolean removeHeader(String header) {
        return headers.remove(header) != null;
    }

    /**
     * Fetches the set of Headers currently associated with this Template.
     * @return The set of headers currently associated with this Template.
     */
    public Iterable<Map.Entry<String, String>> getHeaders() {
        return headers.entrySet();
    }

    /**
     * Adds a tag to be associated with this template.
     * @param tag The tag to be associated with this template.
     * @return True if this tag was not present before calling this method.
     */
    public boolean addTag(String tag) {
        return this.tags.add(tag);
    }

    /**
     * Adds a range of tags to be associated with this template.
     * @param tags The set of tags to be associated with this template.
     * @return True if any of the tags provided were not formerly present.
     */
    public boolean addTags(Collection<? extends  String> tags) {
        return this.tags.addAll(tags);
    }

    /**
     * Fetches the set of tags associated with this template.
     * @return The tags associated with this template.
     */
    public Iterable<String> getTags() {
        return this.tags;
    }

    /**
     * Removes a tag from being associated with this template.
     * @param tag The tag that should not be associated with this template.
     * @return True if this tag had been present.
     */
    public boolean removeTag(String tag) {
        return this.tags.remove(tag);
    }

    /**
     * Removes the provided tags from being associated with this template.
     * @param tags The tags that should not be associated with this template.
     * @return True if any of these tags had been present.
     */
    public boolean removeTags(Collection<?> tags) {
        return this.tags.removeAll(tags);
    }

    /**
     * Removes all tags from this template.
     */
    public void clearTags() {
        this.tags.clear();
    }

    /**
     * The body of this template.
     * @return The value of this template body.
     */
    public String getBody() {
        return body;
    }

    /**
     * Updates the body of this template.
     * @param body The new value for this template's body.
     */
    public void setBody(String body) {
        this.body = body;
    }
}
