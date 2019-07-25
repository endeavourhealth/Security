package org.endeavourhealth.common.security.usermanagermodel.models.caching;

import org.endeavourhealth.common.security.keycloak.client.KeycloakAdminClient;
import org.endeavourhealth.common.security.usermanagermodel.models.DAL.SecurityUserApplicationPolicyDAL;
import org.endeavourhealth.common.security.usermanagermodel.models.DAL.SecurityUserProjectDAL;
import org.endeavourhealth.common.security.usermanagermodel.models.database.UserApplicationPolicyEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.json.JsonUser;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCache {

    private static Map<String, UserRepresentation> userMap = new HashMap<>();
    private static Map<String, String> userApplicationPolicyIdMap = new HashMap<>();
    private static Map<String, UserApplicationPolicyEntity> userApplicationPolicyMap = new HashMap<>();
    private static Map<String, Boolean> userProjectApplicationAccessMap = new HashMap<>();

    public static UserRepresentation getUserDetails(String userId) throws Exception {
        UserRepresentation foundUser = null;

        if (userMap.containsKey(userId)) {
            foundUser = userMap.get(userId);
        } else {

            KeycloakAdminClient keycloakClient = new KeycloakAdminClient();

            try {
                UserRepresentation user = keycloakClient.realms().users().getUser(userId);

                if (user != null) {
                    userMap.put(user.getId(), user);
                    foundUser = user;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw new Exception(e);
            }
        }

        CacheManager.startScheduler();

        return foundUser;

    }

    public static List<JsonUser> getAllUsers() throws Exception {
        UserRepresentation foundUser = null;

        List<JsonUser> userList = new ArrayList<>();
        List<UserRepresentation> users;
        KeycloakAdminClient keycloakClient = new KeycloakAdminClient();

        try {
            users = keycloakClient.realms().users().getUsers("", 0, 100);

            for (UserRepresentation user : users) {
                userMap.put(user.getId(), user);
                userList.add(new JsonUser(user));
            }
        } catch (Exception e) {

        }

        CacheManager.startScheduler();

        return userList;
    }

    public static String getUserApplicationPolicyId(String userId) throws Exception {
        String foundPolicy = null;

        if (userApplicationPolicyIdMap.containsKey(userId)) {
            foundPolicy = userApplicationPolicyIdMap.get(userId);
        } else {
            UserApplicationPolicyEntity userApp = new SecurityUserApplicationPolicyDAL().getUserApplicationPolicy(userId);
            foundPolicy = userApp.getApplicationPolicyId();
            userApplicationPolicyIdMap.put(userId, foundPolicy);
        }

        CacheManager.startScheduler();

        return foundPolicy;
    }

    public static UserApplicationPolicyEntity getUserApplicationPolicy(String userId) throws Exception {
        UserApplicationPolicyEntity foundPolicy = null;

        if (userApplicationPolicyMap.containsKey(userId)) {
            foundPolicy = userApplicationPolicyMap.get(userId);
        } else {
            UserApplicationPolicyEntity userApp = new SecurityUserApplicationPolicyDAL().getUserApplicationPolicy(userId);
            foundPolicy = userApp;
            userApplicationPolicyMap.put(userId, userApp);
        }

        CacheManager.startScheduler();

        return foundPolicy;
    }

    public static void clearUserCache(String userId) throws Exception {
        if (userApplicationPolicyMap.containsKey(userId)) {
            userApplicationPolicyMap.remove(userId);
        }

        if (userApplicationPolicyIdMap.containsKey(userId)) {
            userApplicationPolicyIdMap.remove(userId);
        }

        if (userProjectApplicationAccessMap.containsKey(userId)) {
            userProjectApplicationAccessMap.remove(userId);
        }

        if (userMap.containsKey(userId)) {
            userMap.remove(userId);
        }
    }

    public static Boolean getUserProjectApplicationAccess(String userId, String projectId, String appName) throws Exception {
        String upa = userId + "|" + projectId + "|" + appName;

        Boolean accessToApp = false;

        if (userProjectApplicationAccessMap.containsKey(upa)) {
            accessToApp = userProjectApplicationAccessMap.get(upa);
        } else {
            accessToApp = new SecurityUserProjectDAL().checkUserProjectApplicationAccess(userId, projectId, appName);
            userProjectApplicationAccessMap.put(upa, accessToApp);
        }

        CacheManager.startScheduler();

        return accessToApp;
    }

    public static void flushCache() throws Exception {
        userMap.clear();
        userApplicationPolicyIdMap.clear();
        userProjectApplicationAccessMap.clear();
        userApplicationPolicyMap.clear();
    }
}
