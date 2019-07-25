package org.endeavourhealth.common.security.usermanagermodel.models.caching;


import org.endeavourhealth.common.security.usermanagermodel.models.DAL.SecurityApplicationDAL;
import org.endeavourhealth.common.security.usermanagermodel.models.database.ApplicationEntity;

import java.util.HashMap;
import java.util.Map;

public class ApplicationCache {
    private static Map<String, ApplicationEntity> applicationMap = new HashMap<>();

    public static ApplicationEntity getApplicationDetails(String applicationId) throws Exception {
        ApplicationEntity foundRole = null;

        if (applicationMap.containsKey(applicationId)) {
            foundRole = applicationMap.get(applicationId);
        } else {
            foundRole = new SecurityApplicationDAL().getApplication(applicationId);
            applicationMap.put(foundRole.getId(), foundRole);

        }

        CacheManager.startScheduler();
        return foundRole;

    }

    public static void clearApplicationCache(String applicationId) throws Exception {
        if (applicationMap.containsKey(applicationId)) {
            applicationMap.remove(applicationId);
        }
    }

    public static void flushCache() throws Exception {
        applicationMap.clear();
    }
}
