package com.microsoft.windowsazure.messaging.notificationhubs;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class NotificationHubTest {
    @Test
    public void addInstanceTag() {
        final String tagToAdd = "tag1";
        NotificationHub subject = new NotificationHub(null, null, null);
        final Set<String> expectedTags = new HashSet<String>();
        expectedTags.add(tagToAdd);

        assertFalse(subject.getInstanceTags().iterator().hasNext());

        subject.addInstanceTag(tagToAdd);

        assertTrue(expectedTags.equals(copyToSet(subject.getInstanceTags())));
    }

    @Test
    public void removeInstanceTag() {
        NotificationHub subject = new NotificationHub(null, null, null);
        final String tagToRemove = "removableTag";
        final String tagToStay = "doNotRemove";
        final Set<String> beforeTags = new HashSet<String>();
        beforeTags.add(tagToRemove);
        beforeTags.add(tagToStay);
        subject.addInstanceTags(beforeTags);

        final Set<String> expectedTags = new HashSet<String>();
        expectedTags.add(tagToStay);

        assertTrue(beforeTags.equals(copyToSet(subject.getInstanceTags())));
        subject.removeInstanceTag(tagToRemove);
        assertTrue(expectedTags.equals(copyToSet(subject.getInstanceTags())));
    }

    @Test
    public void reinstallInstance() {
        final int[] callCount = {0};
        final String tagToAdd = "tag1";
        NotificationHub subject = new NotificationHub(null, null, null, installation -> {
            callCount[0]++;
            assertTrue(copyToSet(installation.getTags()).contains(tagToAdd));
        });

        assertEquals(0, callCount[0]);
        subject.addInstanceTag(tagToAdd);
        assertEquals(1, callCount[0]);
    }

    @Test
    public void instanceTemplateLifecycle() {
        NotificationHub subject = new NotificationHub(null, null, null);

        final String template1Name = "template1";
        final InstallationTemplate template1 = new InstallationTemplate();

        InstallationTemplate beforeAdd = subject.getInstanceTemplate(template1Name);
        assertNull(beforeAdd);

        subject.addInstanceTemplate(template1Name, template1);
        InstallationTemplate afterAdd = subject.getInstanceTemplate(template1Name);
        assertEquals(template1, afterAdd);

        subject.removeInstanceTemplate(template1Name);
        InstallationTemplate afterRemove = subject.getInstanceTemplate(template1Name);
        assertNull(afterRemove);
    }

    /**
     * Converts a data structure holding a logical set, and converts it to an actual Java Set.
     * @param i The object containing a logical set of values.
     * @param <T> The type of the values in the collection.
     * @return If `i` was already a Set\<T>, `i` is returned. Otherwise, a new Set\<T> is returned.
     * @throws IllegalArgumentException when a iterable containing duplicate values is passed.
     */
    private static <T> Set<T> copyToSet(Iterable<T> i) {
        if (i instanceof Set<?>) {
            return (Set<T>) i;
        }

        Set<T> retval = new HashSet<T>();

        for (T t: i) {
            if(!retval.add(t)) {
                throw new IllegalArgumentException("Iterable is not a logical set.");
            }
        }
        return retval;
    }
}