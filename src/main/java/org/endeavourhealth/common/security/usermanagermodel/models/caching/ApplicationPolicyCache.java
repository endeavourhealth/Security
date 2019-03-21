package org.endeavourhealth.common.security.usermanagermodel.models.caching;

import org.endeavourhealth.common.security.usermanagermodel.models.DAL.SecurityApplicationPolicyAttributeDAL;
import org.endeavourhealth.common.security.usermanagermodel.models.DAL.SecurityApplicationPolicyDAL;
import org.endeavourhealth.common.security.usermanagermodel.models.database.ApplicationPolicyEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.json.JsonApplicationPolicyAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationPolicyCache {
    private static Map<String, ApplicationPolicyEntity> applicationPolicyMap = new HashMap<>();
    private static Map<String, List<JsonApplicationPolicyAttribute>> policyAttributeMap = new HashMap<>();
    private static boolean allApplicationPoliciesFound = false;

    public static ApplicationPolicyEntity getApplicationPolicyDetails(String applicationPolicyId) throws Exception {
        ApplicationPolicyEntity foundPolicy = null;

        if (applicationPolicyMap.containsKey(applicationPolicyId)) {
            foundPolicy = applicationPolicyMap.get(applicationPolicyId);
        } else {
            foundPolicy = new SecurityApplicationPolicyDAL().getApplicationPolicy(applicationPolicyId);
            applicationPolicyMap.put(foundPolicy.getId(), foundPolicy);

        }

        CacheManager.startScheduler();
        return foundPolicy;

    }

    public static List<JsonApplicationPolicyAttribute> getApplicationPolicyAttributes(String applicationPolicyId) throws Exception {
        List<JsonApplicationPolicyAttribute> foundAttributes = null;

        if (policyAttributeMap.containsKey(applicationPolicyId)) {
            foundAttributes = policyAttributeMap.get(applicationPolicyId);
        } else {
            foundAttributes = new SecurityApplicationPolicyAttributeDAL().getApplicationPolicyAttributes(applicationPolicyId);
            policyAttributeMap.put(applicationPolicyId, foundAttributes);
        }

        CacheManager.startScheduler();
        return foundAttributes;

    }

    public static List<ApplicationPolicyEntity> getAllApplicationPolicies() throws Exception {

        if (!allApplicationPoliciesFound) {
            List<ApplicationPolicyEntity> allPolicies = new SecurityApplicationPolicyDAL().getAllApplicationPolicies();
            for (ApplicationPolicyEntity reg : allPolicies) {
                applicationPolicyMap.put(reg.getId(), reg);
            }
        }

        CacheManager.startScheduler();

        allApplicationPoliciesFound = true;

        return new ArrayList(applicationPolicyMap.values());

    }

    public static void flushCache() throws Exception {
        applicationPolicyMap.clear();
        policyAttributeMap.clear();
        allApplicationPoliciesFound = false;
    }
}
