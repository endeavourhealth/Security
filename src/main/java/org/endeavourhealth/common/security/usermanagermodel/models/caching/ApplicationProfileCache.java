package org.endeavourhealth.common.security.usermanagermodel.models.caching;


import org.endeavourhealth.common.security.usermanagermodel.models.DAL.SecurityApplicationAccessProfileDAL;
import org.endeavourhealth.common.security.usermanagermodel.models.database.ApplicationAccessProfileEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationProfileCache {
    private static Map<String, ApplicationAccessProfileEntity> applicationProfileMap = new ConcurrentHashMap<>();

    public static ApplicationAccessProfileEntity getApplicationProfileDetails(String applicationProfileId) throws Exception {

        ApplicationAccessProfileEntity foundRole = applicationProfileMap.get(applicationProfileId);
        if (foundRole == null) {
            foundRole = new SecurityApplicationAccessProfileDAL().getApplicationProfile(applicationProfileId);
            applicationProfileMap.put(foundRole.getId(), foundRole);

        }

        CacheManager.startScheduler();

        return foundRole;

    }

    public static void clearApplicationProfileCache(String applicationProfileId) throws Exception {
            applicationProfileMap.remove(applicationProfileId);
    }

    public static void flushCache() throws Exception {
        applicationProfileMap.clear();
    }
}
