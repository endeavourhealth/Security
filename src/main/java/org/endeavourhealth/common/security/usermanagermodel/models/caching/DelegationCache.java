package org.endeavourhealth.common.security.usermanagermodel.models.caching;


import org.endeavourhealth.common.security.usermanagermodel.models.DAL.SecurityDelegationDAL;
import org.endeavourhealth.common.security.usermanagermodel.models.database.DelegationEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DelegationCache {

    private static Map<String, DelegationEntity> delegationMap = new ConcurrentHashMap<>();

    public static DelegationEntity getDelegationDetails(String delgationId) throws Exception {

        DelegationEntity foundDelegation = delegationMap.get(delgationId);
        if (foundDelegation == null) {
            foundDelegation = new SecurityDelegationDAL().getDelegation(delgationId);
            delegationMap.put(foundDelegation.getUuid(), foundDelegation);

        }

        CacheManager.startScheduler();

        return foundDelegation;

    }

    public static void clearDelegationCache(String delegationId) throws Exception {
        delegationMap.remove(delegationId);
    }

    public static void flushCache() throws Exception {
        delegationMap.clear();
    }
}
