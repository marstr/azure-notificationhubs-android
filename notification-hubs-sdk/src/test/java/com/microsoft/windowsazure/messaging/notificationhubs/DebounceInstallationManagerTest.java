package com.microsoft.windowsazure.messaging.notificationhubs;

import org.junit.Test;

import static org.junit.Assert.*;

public class DebounceInstallationManagerTest {

    @Test
    public void saveInstallation() {
        final int[] callCount = new int[]{0};

        DebounceInstallationManager subject = new DebounceInstallationManager(installation -> callCount[0]++);

        subject.saveInstallation(new Installation());
        subject.saveInstallation(new Installation());
        subject.saveInstallation(new Installation());

        try {
            Thread.sleep(DebounceInstallationManager.DEFAULT_DEBOUNCE_PERIOD * 2);
        } catch (InterruptedException e) {
            fail("interrupted");
        }

        assertEquals(1, callCount[0]);
    }
}