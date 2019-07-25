package org.endeavourhealth.common.security.usermanagermodel.models.caching;


import org.endeavourhealth.common.security.usermanagermodel.models.DAL.SecurityDelegationDAL;
import org.endeavourhealth.common.security.usermanagermodel.models.database.DelegationEntity;

import java.util.HashMap;
import java.util.Map;

public class DelegationCache {

    private static Map<String, DelegationEntity> delegationMap = new HashMap<>();

    public static DelegationEntity getDelegationDetails(String delgationId) throws Exception {
        DelegationEntity foundDelegation = null;

        if (delegationMap.containsKey(delgationId)) {
            foundDelegation = delegationMap.get(delgationId);
        } else {
            foundDelegation = new SecurityDelegationDAL().getDelegation(delgationId);
            delegationMap.put(foundDelegation.getUuid(), foundDelegation);

        }

        CacheManager.startScheduler();

        return foundDelegation;

    }

    public static void clearDelegationCache(String delegationId) throws Exception {
        if (delegationMap.containsKey(delegationId)) {
            delegationMap.remove(delegationId);
        }

    }

    public static void flushCache() throws Exception {
        delegationMap.clear();
    }
}
