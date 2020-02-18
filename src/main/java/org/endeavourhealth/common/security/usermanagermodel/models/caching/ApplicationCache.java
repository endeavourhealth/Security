package org.endeavourhealth.common.security.usermanagermodel.models.caching;


import org.endeavourhealth.common.security.usermanagermodel.models.DAL.SecurityApplicationDAL;
import org.endeavourhealth.common.security.usermanagermodel.models.database.ApplicationEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationCache {
    private static Map<String, ApplicationEntity> applicationMap = new ConcurrentHashMap<>();

    public static ApplicationEntity getApplicationDetails(String applicationId) throws Exception {

        ApplicationEntity foundRole = applicationMap.get(applicationId);
        if (foundRole == null) {
            foundRole = new SecurityApplicationDAL().getApplication(applicationId);
            applicationMap.put(foundRole.getId(), foundRole);

        }

        CacheManager.startScheduler();
        return foundRole;

    }

    public static void clearApplicationCache(String applicationId) throws Exception {
        applicationMap.remove(applicationId);
    }

    public static void flushCache() throws Exception {
        applicationMap.clear();
    }
}
