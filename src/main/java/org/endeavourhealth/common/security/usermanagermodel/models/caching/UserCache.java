package org.endeavourhealth.common.security.usermanagermodel.models.caching;

import org.endeavourhealth.common.security.keycloak.client.KeycloakAdminClient;
import org.endeavourhealth.common.security.usermanagermodel.models.DAL.SecurityUserApplicationPolicyDAL;
import org.endeavourhealth.common.security.usermanagermodel.models.DAL.SecurityUserProjectDAL;
import org.endeavourhealth.common.security.usermanagermodel.models.DAL.SecurityUserRegionDAL;
import org.endeavourhealth.common.security.usermanagermodel.models.database.UserApplicationPolicyEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.database.UserProjectEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.database.UserRegionEntity;
import org.endeavourhealth.common.security.usermanagermodel.models.json.JsonUser;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserCache {

    private static Map<String, UserRepresentation> userMap = new ConcurrentHashMap<>();
    private static Map<String, String> userApplicationPolicyIdMap = new ConcurrentHashMap<>();
    private static Map<String, UserApplicationPolicyEntity> userApplicationPolicyMap = new ConcurrentHashMap<>();
    private static Map<String, Boolean> userProjectApplicationAccessMap = new ConcurrentHashMap<>();
    private static Map<String, UserRegionEntity> userRegionMap = new ConcurrentHashMap<>();
    private static Map<String, UserProjectEntity> userProjectMap = new ConcurrentHashMap<>();

    public static UserRepresentation getUserDetails(String userId) throws Exception {
        UserRepresentation foundUser;

        foundUser = userMap.get(userId);
        if (foundUser == null) {

            KeycloakAdminClient keycloakClient = new KeycloakAdminClient();

            try {
                UserRepresentation user = keycloakClient.realms().users().getUser(userId);

                if (user != null) {
                    userMap.put(user.getId(), user);
                    foundUser = user;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        CacheManager.startScheduler();

        return foundUser;

    }

    public static List<JsonUser> getAllUsers() throws Exception {

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

        String foundPolicy = userApplicationPolicyIdMap.get(userId);
        if (foundPolicy == null) {
            UserApplicationPolicyEntity userApp = new SecurityUserApplicationPolicyDAL().getUserApplicationPolicy(userId);
            foundPolicy = userApp.getApplicationPolicyId();
            userApplicationPolicyIdMap.put(userId, foundPolicy);
        }

        CacheManager.startScheduler();

        return foundPolicy;
    }

    public static UserApplicationPolicyEntity getUserApplicationPolicy(String userId) throws Exception {

        UserApplicationPolicyEntity foundPolicy = userApplicationPolicyMap.get(userId);
        if (foundPolicy == null) {
            UserApplicationPolicyEntity userApp = new SecurityUserApplicationPolicyDAL().getUserApplicationPolicy(userId);
            foundPolicy = userApp;
            userApplicationPolicyMap.put(userId, userApp);
        }

        CacheManager.startScheduler();

        return foundPolicy;
    }

    public static UserProjectEntity getUserProject(String userProjectId) throws Exception {
        UserProjectEntity foundUserProject = userProjectMap.get(userProjectId);

        if (foundUserProject == null) {
            UserProjectEntity userProj = new SecurityUserProjectDAL().getUserProject(userProjectId);
            foundUserProject = userProj;
            userProjectMap.put(userProjectId, userProj);
        }

        CacheManager.startScheduler();

        return foundUserProject;
    }

    public static UserRegionEntity getUserRegion(String userId) throws Exception {

        UserRegionEntity foundRegion = userRegionMap.get(userId);
        if (foundRegion == null) {
            UserRegionEntity userRegion = new SecurityUserRegionDAL().getUserRegion(userId);
            foundRegion = userRegion;
            userRegionMap.put(userId, userRegion);
        }

        CacheManager.startScheduler();

        return foundRegion;
    }

    public static Boolean getUserProjectApplicationAccess(String userId, String projectId, String appName) throws Exception {
        String upa = userId + "|" + projectId + "|" + appName;

        Boolean accessToApp = userProjectApplicationAccessMap.get(upa);
        if (accessToApp == null) {
            accessToApp = new SecurityUserProjectDAL().checkUserProjectApplicationAccess(userId, projectId, appName);
            userProjectApplicationAccessMap.put(upa, accessToApp);
        }

        CacheManager.startScheduler();

        return accessToApp;
    }

    public static void clearUserCache(String userId) throws Exception {
        userApplicationPolicyMap.remove(userId);
        userApplicationPolicyIdMap.remove(userId);
        userProjectApplicationAccessMap.remove(userId);
        userMap.remove(userId);
        userRegionMap.remove(userId);
        userProjectMap.clear();
    }

    public static void flushCache() throws Exception {
        userMap.clear();
        userApplicationPolicyIdMap.clear();
        userProjectApplicationAccessMap.clear();
        userApplicationPolicyMap.clear();
        userRegionMap.clear();
        userProjectMap.clear();
    }
}
