package org.endeavourhealth.common.security.keycloak.client.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.representations.idm.RoleRepresentation;

import java.io.IOException;
import java.util.List;

public class Roles extends KeycloakAdminClientBase {

    private static final TypeReference<RoleRepresentation> roleRepresentationTypeReference = new TypeReference<RoleRepresentation>() {};
    private static final TypeReference<List<RoleRepresentation>> listRoleRepresentationTypeReference = new TypeReference<List<RoleRepresentation>>() {};


    public Roles() {
        composites = new RoleComposites();
    }

    private RoleComposites composites;

    public RoleComposites composites() {
        return composites;
    }

    //
    // get roles
    //

    public List<RoleRepresentation> getRealmRoles() {
        assertKeycloakAdminClientInitialised();
        return getRealmRoles(getRealm());
    }

    public List<RoleRepresentation> getRealmRoles(String realm) {
        assertKeycloakAdminClientInitialised();

        List<RoleRepresentation> roles = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/roles");
            roles = toEntity(response, listRoleRepresentationTypeReference);
        } catch (IOException e) {
            LOG.error("Keycloak get roles failed", e);
        }
        return roles;
    }

    //
    // get role
    //

    public RoleRepresentation getRole(String roleName) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return getRole(getRealm(), roleName);
    }

    public RoleRepresentation getRole(String realm, String roleName) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        RoleRepresentation roleRepresentation = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            roleRepresentation = internalGetRole(realm, roleName, httpClient);
        } catch (IOException e) {
            LOG.error("Keycloak get role failed", e);
        }
        return roleRepresentation;
    }

    private RoleRepresentation internalGetRole(String realm, String roleName, CloseableHttpClient httpClient) throws IOException, KeycloakClientException {
        HttpResponse response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/roles/" + roleName.trim());
        if(isHttpOkStatus(response)) {
            return toEntity(response, roleRepresentationTypeReference);
        } else {
            throw new KeycloakClientException("Failed to get role", response.getStatusLine().getReasonPhrase());
        }
    }

    //
    // get role by id
    //

    public RoleRepresentation getRoleById(String roleId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return getRoleById(getRealm(), roleId);
    }

    public RoleRepresentation getRoleById(String realm, String roleId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        RoleRepresentation roleRepresentation = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/roles-by-id/" + roleId.trim());
            roleRepresentation = toEntity(response, roleRepresentationTypeReference);
        } catch (IOException e) {
            LOG.error("Keycloak get role by ID failed", e);
        }
        return roleRepresentation;
    }

    //
    // post role
    //

    public RoleRepresentation postRealmRole(RoleRepresentation role) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return postRealmRole(getRealm(), role);
    }

    public RoleRepresentation postRealmRole(String realm, RoleRepresentation role) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doPost(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/roles", role);
            if(isHttpOkStatus(response)) {
                role = internalGetRole(realm, getIdFromLocation(response), httpClient);
            } else {
                throw new KeycloakClientException("Failed to post role", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak post role failed", e);
        }
        return role;
    }

    //
    // put role
    //

    public RoleRepresentation putRealmRole(RoleRepresentation role) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return putRealmRole(getRealm(), role);
    }

    public RoleRepresentation putRealmRole(String realm, RoleRepresentation role) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doPut(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/roles-by-id/" + role.getId(), role);
            if(isHttpOkStatus(response)) {
                response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/roles-by-id/" + role.getId());
                role = toEntity(response, roleRepresentationTypeReference);
            } else {
                throw new KeycloakClientException("Failed to put role", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak put role failed", e);
        }
        return role;
    }

    //
    // delete role
    //

    public void deleteRealmRole(String roleName) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        deleteRealmRole(getRealm(), roleName);
    }

    public void deleteRealmRole(String realm, String roleName) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doDelete(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/roles/" + roleName.trim());
            if(!isHttpOkStatus(response)) {
                throw new KeycloakClientException("Failed to delete role", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak delete role failed", e);
        }
    }

    //
    // delete role by id
    //

    public void deleteRealmRoleById(String roleId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        deleteRealmRoleById(getRealm(), roleId);
    }

    public void deleteRealmRoleById(String realm, String roleId) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doDelete(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/roles-by-id/" + roleId.trim());
            if(!isHttpOkStatus(response)) {
                throw new KeycloakClientException("Failed to delete role", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak delete role by ID failed", e);
        }
    }

}
