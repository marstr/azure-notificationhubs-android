package com.microsoft.windowsazure.messaging.notificationhubs;

import java.util.HashMap;
import java.util.Map;

public class TemplateMiddleware implements InstallationMiddleware {

    private Map<String, InstallationTemplate> mTemplates;

    public TemplateMiddleware() {
        mTemplates = new HashMap<String, InstallationTemplate>();
    }

    public boolean addTemplate(String name, InstallationTemplate template) {
        InstallationTemplate prev = mTemplates.put(name, template);
        if(prev != null && prev.equals(template)) {
            return false;
        }
        return true;
    }

    public boolean removeTemplate(String name) {
        InstallationTemplate prev = mTemplates.remove(name);
        if (prev == null) {
            return false;
        }
        return true;
    }

    public InstallationTemplate getTemplate(String name) {
        return mTemplates.get(name);
    }

    @Override
    public InstallationEnricher getInstallationEnricher(InstallationEnricher next) {
        return subject -> {
            for(Map.Entry<String, InstallationTemplate> entry : mTemplates.entrySet()) {
                subject.addTemplate(entry.getKey(), entry.getValue());
            }
            if (next != null) {
                next.enrichInstallation(subject);
            }
        };
    }
}
