package org.endeavourhealth.common.security.usermanagermodel.models.caching;

import org.endeavourhealth.common.security.usermanagermodel.models.DAL.SecurityApplicationPolicyAttributeDAL;
import org.endeavourhealth.common.security.usermanagermodel.models.DAL.SecurityApplicationPolicyDAL;
import org.endeavourhealth.common.security.usermanagermodel.models.database.ApplicationPolicyEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.json.JsonApplicationPolicyAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationPolicyCache {
    private static Map<String, ApplicationPolicyEntity> applicationPolicyMap = new ConcurrentHashMap<>();
    private static List<ApplicationPolicyEntity> nonSUApplicationPolicies = new ArrayList<>();
    private static Map<String, List<JsonApplicationPolicyAttribute>> policyAttributeMap = new ConcurrentHashMap<>();
    private static boolean allApplicationPoliciesFound = false;
    private static boolean nonSUApplicationPoliciesFound = false;

    public static ApplicationPolicyEntity getApplicationPolicyDetails(String applicationPolicyId) throws Exception {

        ApplicationPolicyEntity foundPolicy = applicationPolicyMap.get(applicationPolicyId);
        if (foundPolicy == null) {
            foundPolicy = new SecurityApplicationPolicyDAL().getApplicationPolicy(applicationPolicyId);
            applicationPolicyMap.put(foundPolicy.getId(), foundPolicy);

        }

        CacheManager.startScheduler();
        return foundPolicy;

    }

    public static List<JsonApplicationPolicyAttribute> getApplicationPolicyAttributes(String applicationPolicyId) throws Exception {

        List<JsonApplicationPolicyAttribute> foundAttributes = policyAttributeMap.get(applicationPolicyId);
        if (foundAttributes == null) {
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

    public static List<ApplicationPolicyEntity> getNonSUApplicationPolicies() throws Exception {

        if (nonSUApplicationPoliciesFound) {
            return nonSUApplicationPolicies;
        }

        List<ApplicationPolicyEntity> allPolicies = getAllApplicationPolicies();

        for (ApplicationPolicyEntity policy : allPolicies) {
            List<JsonApplicationPolicyAttribute> attributes = getApplicationPolicyAttributes(policy.getId());

            Boolean containsSuperUser = false;
            for (JsonApplicationPolicyAttribute atr : attributes) {
                if (atr.getApplicationAccessProfileSuperUser()) {
                    containsSuperUser = true;
                    break;
                }
            }

            if (!containsSuperUser) {
                nonSUApplicationPolicies.add(policy);
            }
        }

        CacheManager.startScheduler();

        nonSUApplicationPoliciesFound = true;

        return nonSUApplicationPolicies;

    }

    public static void clearApplicationPolicyCache(String applicationPolicyId) throws Exception {
        applicationPolicyMap.clear();
        policyAttributeMap.clear();
        allApplicationPoliciesFound = false;

    }

    public static void flushCache() throws Exception {
        applicationPolicyMap.clear();
        policyAttributeMap.clear();
        allApplicationPoliciesFound = false;
    }
}
