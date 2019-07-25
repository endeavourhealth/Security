package org.endeavourhealth.common.security.usermanagermodel.models.caching;


import org.endeavourhealth.common.security.usermanagermodel.models.DAL.SecurityApplicationAccessProfileDAL;
import org.endeavourhealth.common.security.usermanagermodel.models.database.ApplicationAccessProfileEntity;

import java.util.HashMap;
import java.util.Map;

public class ApplicationProfileCache {
    private static Map<String, ApplicationAccessProfileEntity> applicationProfileMap = new HashMap<>();

    public static ApplicationAccessProfileEntity getApplicationProfileDetails(String applicationProfileId) throws Exception {
        ApplicationAccessProfileEntity foundRole = null;

        if (applicationProfileMap.containsKey(applicationProfileId)) {
            foundRole = applicationProfileMap.get(applicationProfileId);
        } else {
            foundRole = new SecurityApplicationAccessProfileDAL().getApplicationProfile(applicationProfileId);
            applicationProfileMap.put(foundRole.getId(), foundRole);

        }

        CacheManager.startScheduler();

        return foundRole;

    }

    public static void clearApplicationProfileCache(String applicationProfileId) throws Exception {
        if (applicationProfileMap.containsKey(applicationProfileId)) {
            applicationProfileMap.remove(applicationProfileId);
        }

    }

    public static void flushCache() throws Exception {
        applicationProfileMap.clear();
    }
}
