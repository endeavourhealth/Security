package org.endeavourhealth.common.security.keycloak.client.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.io.IOException;
import java.util.List;

public class Groups extends KeycloakAdminClientBase {

    private static final TypeReference<GroupRepresentation> groupRepresentationTypeReference = new TypeReference<GroupRepresentation>() {};
    private static final TypeReference<List<GroupRepresentation>> listGroupRepresentationTypeReference = new TypeReference<List<GroupRepresentation>>() {};
    private static final TypeReference<List<UserRepresentation>> listUserRepresentationTypeReference = new TypeReference<List<UserRepresentation>>() {};
    private static final TypeReference<List<RoleRepresentation>> listRoleRepresentationTypeReference = new TypeReference<List<RoleRepresentation>>() {};

    //
    // get groups
    //

    public List<GroupRepresentation> getGroups() {
        assertKeycloakAdminClientInitialised();
        return getGroups(getRealm());
    }

    public List<GroupRepresentation> getGroups(String realm) {
        assertKeycloakAdminClientInitialised();

        List<GroupRepresentation> groups = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/groups");
            groups = toEntity(response, listGroupRepresentationTypeReference);
        } catch (IOException e) {
            LOG.error("Keycloak get groups failed", e);
        }
        return groups;
    }

    //
    // get group (now supports Id)
    //

    public GroupRepresentation getGroup(String groupId) {
        assertKeycloakAdminClientInitialised();
        return getGroup(getRealm(), groupId);
    }

    public GroupRepresentation getGroup(String realm, String groupId) {
        assertKeycloakAdminClientInitialised();

        GroupRepresentation groupRepresentation = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/groups/" + groupId.trim());
            groupRepresentation = toEntity(response, groupRepresentationTypeReference);
        } catch (IOException e) {
            LOG.error("Keycloak get group failed", e);
        }
        return groupRepresentation;
    }

    //
    // post group
    //

    public GroupRepresentation postGroup(GroupRepresentation group) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return postGroup(getRealm(), group);
    }

    public GroupRepresentation postGroup(String realm, GroupRepresentation group) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doPost(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/groups", group);
            if(isHttpOkStatus(response)) {
                response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/groups/" + getIdFromLocation(response));
                group = toEntity(response, groupRepresentationTypeReference);
            } else {
                throw new KeycloakClientException("Failed to post group", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak post group failed", e);
        }
        return group;
    }

    //
    // put group
    //

    public GroupRepresentation putGroup(GroupRepresentation group) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return putGroup(getRealm(), group);
    }

    public GroupRepresentation putGroup(String realm, GroupRepresentation group) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doPut(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/groups/" + group.getId(), group);
            if(isHttpOkStatus(response)) {
                response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/groups/" + group.getId());
                group = toEntity(response, groupRepresentationTypeReference);
            } else {
                throw new KeycloakClientException("Failed to put group", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak put group failed", e);
        }
        return group;
    }

    //
    // delete group (now supports Id)
    //

    public void deleteGroup(String groupId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        deleteGroup(getRealm(), groupId);
    }

    public void deleteGroup(String realm, String groupId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doDelete(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/groups/" + groupId.trim());
            if(!isHttpOkStatus(response)) {
                throw new KeycloakClientException("Failed to delete group", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak delete group failed", e);
        }
    }

    //
    // child group
    //

    public GroupRepresentation postChildGroup(String parentGroupId, GroupRepresentation group) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return postChildGroup(getRealm(), parentGroupId, group);
    }

    public GroupRepresentation postChildGroup(String realm, String parentGroupId, GroupRepresentation group) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doPost(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/groups/" + parentGroupId.trim() + "/children", group);
            group = toEntity(response, groupRepresentationTypeReference);
            if(!isHttpOkStatus(response)) {
                throw new KeycloakClientException("Failed to post child group", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak post group failed", e);
        }
        return group;
    }

    //
    // get group membership
    //

    public List<UserRepresentation> getGroupMembers(String groupId, int offset, int limit) {
        assertKeycloakAdminClientInitialised();
        return getGroupMembers(getRealm(), groupId, offset, limit);
    }

    public List<UserRepresentation> getGroupMembers(String realm, String groupId, int offset, int limit) {
        assertKeycloakAdminClientInitialised();

        List<UserRepresentation> users = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            String queryString = String.format("first=%d&max=%d", offset, limit);
            HttpResponse response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/groups/" + groupId.trim() + "/members?" + queryString);
            users = toEntity(response, listUserRepresentationTypeReference);
        } catch (IOException e) {
            LOG.error("Keycloak get group failed", e);
        }
        return users;
    }

    //
    // get effective realm role-mappings
    //

    public List<RoleRepresentation> getEffectiveRealmRoleMappingIds(String groupId) {
        assertKeycloakAdminClientInitialised();
        return getEffectiveRealmRoleMappingIds(getRealm(), groupId);
    }

    public List<RoleRepresentation> getEffectiveRealmRoleMappingIds(String realm, String groupId) {
        assertKeycloakAdminClientInitialised();

        List<RoleRepresentation> roleMappings = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/groups/" + groupId.trim() + "/role-mappings/realm/composite");
            roleMappings = toEntity(response, listRoleRepresentationTypeReference);
        } catch (IOException e) {
            LOG.error("Keycloak get realm role-mappings failed", e);
        }
        return roleMappings;
    }

    //
    // get realm role-mappings
    //

    public List<RoleRepresentation> getRealmRoleMappingIds(String groupId) {
        assertKeycloakAdminClientInitialised();
        return getRealmRoleMappingIds(getRealm(), groupId);
    }

    public List<RoleRepresentation> getRealmRoleMappingIds(String realm, String groupId) {
        assertKeycloakAdminClientInitialised();

        List<RoleRepresentation> roleMappings = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/groups/" + groupId.trim() + "/role-mappings/realm");
            roleMappings = toEntity(response, listRoleRepresentationTypeReference);
        } catch (IOException e) {
            LOG.error("Keycloak get realm role-mappings failed", e);
        }
        return roleMappings;
    }

    //
    // add realm role-mappings
    //

    public List<RoleRepresentation> addRealmRoleMapping(String groupId, List<RoleRepresentation> roles) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return addRealmRoleMapping(getRealm(), groupId, roles);
    }

    public List<RoleRepresentation> addRealmRoleMapping(String realm, String groupId, List<RoleRepresentation> roles) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        List<RoleRepresentation> roleMappings = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doPost(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/groups/" + groupId.trim() + "/role-mappings/realm", roles);
            if(isHttpOkStatus(response)) {
                response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/groups/" + groupId.trim() + "/role-mappings/realm");
                roleMappings = toEntity(response, listRoleRepresentationTypeReference);
            } else {
                throw new KeycloakClientException("Failed to add realm role-mapping", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak add realm role-mappings failed", e);
        }
        return roleMappings;
    }

    //
    // remove realm role-mappings
    //

    public List<RoleRepresentation> deleteRealmRoleMapping(String groupId, List<RoleRepresentation> roles) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return deleteRealmRoleMapping(getRealm(), groupId, roles);
    }

    public List<RoleRepresentation> deleteRealmRoleMapping(String realm, String groupId, List<RoleRepresentation> roles) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        List<RoleRepresentation> roleMappings = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doDelete(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/groups/" + groupId.trim() + "/role-mappings/realm", roles);
            if(isHttpOkStatus(response)) {
                response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/groups/" + groupId.trim() + "/role-mappings/realm");
                roleMappings = toEntity(response, listRoleRepresentationTypeReference);
            } else {
                throw new KeycloakClientException("Failed to delete realm role-mapping", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak delete realm role-mappings failed", e);
        }
        return roleMappings;
    }
}
