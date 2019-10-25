package org.endeavourhealth.common.security.keycloak.client.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class Users extends KeycloakAdminClientBase {

    private static final TypeReference<UserRepresentation> userRepresentationTypeReference = new TypeReference<UserRepresentation>() {};
    private static final TypeReference<List<UserRepresentation>> listUserRepresentationTypeReference = new TypeReference<List<UserRepresentation>>() {};
    private static final TypeReference<List<RoleRepresentation>> listRoleRepresentationTypeReference = new TypeReference<List<RoleRepresentation>>() {};
    private static final TypeReference<List<GroupRepresentation>> listGroupRepresentationTypeReference = new TypeReference<List<GroupRepresentation>>() {};

    //
    // get users
    //

    public List<UserRepresentation> getUsers(int offset, int limit) {
        return getUsers(getRealm(), null, offset, limit);
    }

    public List<UserRepresentation> getUsers(String search, int offset, int limit) {
        assertKeycloakAdminClientInitialised();
        return getUsers(getRealm(), search, offset, limit);

    }

    public List<UserRepresentation> getUsers(String realm, String search, int offset, int limit) {
        assertKeycloakAdminClientInitialised();

        List<UserRepresentation> users = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            // The URL is this: http://localhost:9080/auth/admin/realms/endeavour/users?first=0&max=20 (NOTE: paging in the query string)
            String url = getAuthServerBaseUrl() + "/admin/realms/" + realm + "/users?" + String.format("first=%d&max=%d", offset, limit);
            if (StringUtils.isNotBlank(search)) {
                url += "&search=" + URLEncoder.encode(search, "UTF-8");
            }
            HttpResponse response = doGet(httpClient, url);
            users = toEntity(response, listUserRepresentationTypeReference);
        } catch (IOException e) {
            LOG.error("Keycloak get users failed", e);
        }
        return users;
    }

    //
    // get user
    //

    public UserRepresentation getUser(String userId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return getUser(getRealm(), userId);
    }

    public UserRepresentation getUser(String realm, String userId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        UserRepresentation userRepresentation = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/users/" + userId.trim());
            if (isHttpOkStatus(response)) {
                userRepresentation = toEntity(response, userRepresentationTypeReference);
            } else {
                throw new KeycloakClientException("Failed to get user", response.getStatusLine().getReasonPhrase());
            }

        } catch (IOException e) {
            LOG.error("Keycloak get user failed", e);
        }
        return userRepresentation;
    }

    //
    // post user
    //

    public UserRepresentation postUser(UserRepresentation user) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return postUser(getRealm(), user);
    }

    public UserRepresentation postUser(String realm, UserRepresentation user) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doPost(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/users", user);

            if (isHttpOkStatus(response)) {
                response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/users/" + getIdFromLocation(response));
                user = toEntity(response, userRepresentationTypeReference);
            } else {
                throw new KeycloakClientException("Failed to create user", response.getStatusLine().getReasonPhrase());
            }

        } catch (IOException e) {
            LOG.error("Keycloak post user failed", e);
        }
        return user;
    }

    //
    // put user
    //

    public UserRepresentation putUser(UserRepresentation user) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return putUser(getRealm(), user);
    }

    public UserRepresentation putUser(String realm, UserRepresentation user) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doPut(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/users/" + user.getId(), user);
            if (isHttpOkStatus(response)) {
                response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/users/" + user.getId());
                user = toEntity(response, userRepresentationTypeReference);
            } else {
                throw new KeycloakClientException("Failed to update user", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak put user failed", e);
        }
        return user;
    }

    //
    // put user update password email
    //

    public boolean putUserUpdatePasswordEmail(UserRepresentation user) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return putUserUpdatePasswordEmail(getRealm(), user);
    }

    public boolean putUserUpdatePasswordEmail(String realm, UserRepresentation user) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response
                    = doPut(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/users/" + user.getId() + "/execute-actions-email", "[\"UPDATE_PASSWORD\"]");

            if (!isHttpOkStatus(response)) {
                throw new KeycloakClientException("Failed to send update password email", response.getStatusLine().getReasonPhrase());
            }

        } catch (IOException e) {
            LOG.error("Keycloak put user update password email failed", e);
        }
        return false;
    }

    //
    // set user password
    //

    public void setUserPassword(String userId, CredentialRepresentation credential) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        setUserPassword(getRealm(), userId, credential);
    }

    public void setUserPassword(String realm, String userId, CredentialRepresentation credential) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doPut(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/users/" + userId + "/reset-password", credential);
            if (!isHttpOkStatus(response)) {
                throw new KeycloakClientException("Failed to update user password", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak set user password failed", e);
        }
    }

    //
    // delete user
    //

    public boolean deleteUser(String userId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return deleteUser(getRealm(), userId);
    }

    public boolean deleteUser(String realm, String userId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doDelete(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/users/" + userId.trim());
            if (!isHttpOkStatus(response)) {
                throw new KeycloakClientException("Failed to delete user", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak delete user failed", e);
        }

        return false;
    }

    //
    // get user groups (i.e. user is a member of these groups)
    //

    public List<GroupRepresentation> getUserGroups(String userId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return getUserGroups(getRealm(), userId);
    }

    public List<GroupRepresentation> getUserGroups(String realm, String userId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        List<GroupRepresentation> userGroups = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/users/" + userId.trim() + "/groups");
            if (isHttpOkStatus(response)) {
                userGroups = toEntity(response, listGroupRepresentationTypeReference);
            } else {
                throw new KeycloakClientException("Failed to get user groups", response.getStatusLine().getReasonPhrase());
            }

        } catch (IOException e) {
            LOG.error("Keycloak get user groups failed", e);
        }
        return userGroups;
    }

    //
    // join group
    //

    public boolean joinGroup(String userId, String groupId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return joinGroup(getRealm(), userId, groupId);
    }

    public boolean joinGroup(String realm, String userId, String groupId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doPut(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/users/" + userId.trim() + "/groups/" + groupId.trim(), null);
            if (!isHttpOkStatus(response)) {
                throw new KeycloakClientException("Failed to join group", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak user join group failed", e);
        }

        return false;
    }


    //
    // leave group
    //

    public boolean leaveGroup(String userId, String groupId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return leaveGroup(getRealm(), userId, groupId);
    }

    public boolean leaveGroup(String realm, String userId, String groupId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doDelete(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/users/" + userId.trim() + "/groups/" + groupId.trim());
            if (!isHttpOkStatus(response)) {
                throw new KeycloakClientException("Failed to leave group", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak user leave group failed", e);
        }

        return false;
    }

    //
    // get user realm roles
    //

    public List<RoleRepresentation> getUserRealmRoles(String userId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return getUserRealmRoles(getRealm(), userId);
    }

    public List<RoleRepresentation> getUserRealmRoles(String realm, String userId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        List<RoleRepresentation> userRoles = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/users/" + userId.trim() + "/role-mappings/realm");
            if (isHttpOkStatus(response)) {
                userRoles = toEntity(response, listRoleRepresentationTypeReference);
            } else {
                throw new KeycloakClientException("Failed to get user roles", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak user get roles failed", e);
        }

        return userRoles;
    }

    //
    // get user realm roles - Available, i.e. not assigned
    //

    public List<RoleRepresentation> getUserRealmRolesAvailable(String userId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return getUserRealmRolesAvailable(getRealm(), userId);
    }

    public List<RoleRepresentation> getUserRealmRolesAvailable(String realm, String userId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        List<RoleRepresentation> userRolesAvailable = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/users/" + userId.trim() + "/role-mappings/realm/available");
            if (isHttpOkStatus(response)) {
                userRolesAvailable = toEntity(response, listRoleRepresentationTypeReference);
            } else {
                throw new KeycloakClientException("Failed to get user roles available", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak user get roles available failed", e);
        }

        return userRolesAvailable;
    }

    //
    // add realm role
    //

    public boolean addRealmRole(String userId, List<RoleRepresentation> roles) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return addRealmRole(getRealm(), userId, roles);
    }

    public boolean addRealmRole(String realm, String userId, List<RoleRepresentation> roles) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doPost(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/users/" + userId.trim() + "/role-mappings/realm", roles);
            if (!isHttpOkStatus(response)) {
                throw new KeycloakClientException("Failed to add roles", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak user add role failed", e);
        }

        return false;
    }


    //
    // remove realm role
    //

    public boolean removeRealmRole(String userId, List<RoleRepresentation> roles) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return removeRealmRole(getRealm(), userId, roles);
    }

    public boolean removeRealmRole(String realm, String userId, List<RoleRepresentation> roles) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doDelete(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/users/" + userId.trim() + "/role-mappings/realm", roles);
            if (!isHttpOkStatus(response)) {
                throw new KeycloakClientException("Failed to remove roles", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak user remove role failed", e);
        }

        return false;
    }
}
