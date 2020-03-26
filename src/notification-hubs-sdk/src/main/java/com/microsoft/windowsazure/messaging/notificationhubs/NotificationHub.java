package com.microsoft.windowsazure.messaging.notificationhubs;

import android.app.Application;

public final class NotificationHub {
    private static NotificationHub instance;

    private InstallationManager manager;

    private PushChannelMiddleware pushChannelMiddleware;
    private TagMiddleware tagMiddleware;
    private TemplateMiddleware templateMiddleware;
    private InstallationEnricher enricher;

    /**
     * Stores the most recent result of fully enriching an {@link Installation}.
     */
    private Installation currentInstallation;


    private NotificationHub(Application app, String connectionString, String hubName) {
        this(app, connectionString, hubName, defaultMiddleware(), defaultManager());
    }

    private NotificationHub(Application app, String connectionString, String hubName, InstallationMiddleware middleware) {
        this(app, connectionString, hubName, middleware, defaultManager());
    }

    private NotificationHub(Application app, String connectionString, String hubName, InstallationManager manager) {
        this(app, connectionString, hubName, defaultMiddleware(), manager);
    }

    private NotificationHub(Application app, String connectionString, String hubName, InstallationMiddleware middleware, InstallationManager manager) {
        this.pushChannelMiddleware = new PushChannelMiddleware();
        this.tagMiddleware = new TagMiddleware();
        this.templateMiddleware = new TemplateMiddleware();

        this.enricher = middleware.getInstallationEnricher(subject -> {
            // Intentionally Left Blank, to prevent customers from needing to do a null-check.
        });
        this.enricher = this.templateMiddleware.getInstallationEnricher(this.enricher);
        this.enricher = tagMiddleware.getInstallationEnricher(this.enricher);
        this.enricher = pushChannelMiddleware.getInstallationEnricher(this.enricher);

        this.manager = manager;
    }

    public static NotificationHub getInstance() {
        return instance;
    }

    public static void initialize(Application app, String connectionString, String hubName) {
        instance = new NotificationHub(app, connectionString, hubName);
    }

    public static void initialize(Application app, String connectionString, String hubName, InstallationMiddleware middleware) {
        instance = new NotificationHub(app, connectionString, hubName, middleware);
    }

    public static void initialize(Application app, String connectionString, String hubName, InstallationManager manager) {
        instance = new NotificationHub(app, connectionString, hubName, manager);
    }

    public static void initialize(Application app, String connectionString, String hubName, InstallationMiddleware middleware, InstallationManager manager) {
        instance = new NotificationHub(app, connectionString, hubName, middleware, manager);
    }

    /**
     * Fetches the tags that are associated with the current device installation, as last confirmed
     * with Notification Hubs.
     *
     * @return A distinct list of Strings associated with this Installation.
     */
    public static Iterable<String> getTags() {
        return getInstance().getInstanceTags();
    }

    /**
     * Fetches the tags that are associated with the current device installation, as last confirmed
     * with Notification Hubs.
     *
     * @return A distinct list of Strings associated with this Installation.
     */
    public Iterable<String> getInstanceTags() {
        if (currentInstallation != null) {
            return currentInstallation.getTags();
        }
        return tagMiddleware.getTags();
    }

    public static boolean addTag(String tag) {
        return getInstance().addInstanceTag(tag);
    }

    public boolean addInstanceTag(String tag) {
        if(tagMiddleware.addTag(tag)) {
            reinstallInstance();
            return true;
        }
        return false;
    }

    public static boolean removeTag(String tag){
        return getInstance().removeInstanceTag(tag);
    }

    public boolean removeInstanceTag(String tag) {
        if(tagMiddleware.removeTag(tag)) {
            reinstallInstance();
            return true;
        }
        return false;
    }

    public InstallationTemplate getInstanceTemplate(String name) {
        return currentInstallation.getTemplate(name);
    }

    /**
     * Forces a rebuild of the Installation.
     */
    public static void reinstall() {
        getInstance().reinstallInstance();
    }

    public void reinstallInstance() {
        currentInstallation = new Installation();
        enricher.enrichInstallation(currentInstallation);
        manager.saveInstallation(currentInstallation);
    }

    private static InstallationManager defaultManager() {
        return new NoopInstallationManager();
    }

    private static InstallationMiddleware defaultMiddleware() {
        return new NoopMiddleware();
    }
}
