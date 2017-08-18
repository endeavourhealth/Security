package org.endeavourhealth.common.security.keycloak.client.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.representations.idm.RoleRepresentation;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RoleComposites extends KeycloakAdminClientBase {

    private static final TypeReference<List<RoleRepresentation>> listRoleRepresentationTypeReference = new TypeReference<List<RoleRepresentation>>() {};

    //
    // get composites
    //

    public List<RoleRepresentation> getComposites(String roleName) {
        assertKeycloakAdminClientInitialised();
        return getComposites(getRealm(), roleName);
    }

    public List<RoleRepresentation> getComposites(String realm, String roleName) {
        assertKeycloakAdminClientInitialised();

        List<RoleRepresentation> composites = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/roles/" + roleName + "/composites");
            composites = toEntity(response, listRoleRepresentationTypeReference);
        } catch (IOException e) {
            LOG.error("Keycloak get composites failed", e);
        }
        return composites;
    }

    //
    // get composites by Role Id
    //

    public List<RoleRepresentation> getCompositesByRoleId(String roleId) {
        assertKeycloakAdminClientInitialised();
        return getCompositesByRoleId(getRealm(), roleId);
    }

    public List<RoleRepresentation> getCompositesByRoleId(String realm, String roleId) {
        assertKeycloakAdminClientInitialised();

        List<RoleRepresentation> composites = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/roles-by-id/" + roleId.trim() + "/composites");
            composites = toEntity(response, listRoleRepresentationTypeReference);
        } catch (IOException e) {
            LOG.error("Keycloak get composites by ID failed", e);
        }
        return composites;
    }

    //
    // post composite
    //

    public List<RoleRepresentation> postComposite(String roleName, RoleRepresentation composite) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return postComposite(getRealm(), roleName, Arrays.asList(composite));
    }

    public List<RoleRepresentation> postComposite(String roleName, List<RoleRepresentation> composites) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return postComposite(getRealm(), roleName, composites);
    }

    public List<RoleRepresentation> postComposite(String realm, String roleName, List<RoleRepresentation> composites) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doPost(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/roles/" + roleName + "/composites", composites);
            if(isHttpOkStatus(response)) {
                response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/roles/" + roleName + "/composites");
                return toEntity(response, listRoleRepresentationTypeReference);
            } else {
                throw new KeycloakClientException("Failed to post composite", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak post composite failed", e);
        }
        return null;
    }

    //
    // post composite by Role Id
    //

    public List<RoleRepresentation> postCompositeByRoleId(String roleId, RoleRepresentation composite) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return postCompositeByRoleId(getRealm(), roleId, Arrays.asList(composite));
    }

    public List<RoleRepresentation> postCompositeByRoleId(String roleId, List<RoleRepresentation> composites) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        return postCompositeByRoleId(getRealm(), roleId, composites);
    }

    public List<RoleRepresentation> postCompositeByRoleId(String realm, String roleId, List<RoleRepresentation> composites) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doPost(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/roles-by-id/" + roleId.trim() + "/composites", composites);
            if(isHttpOkStatus(response)) {
                response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/roles-by-id/" + roleId.trim() + "/composites");
                return toEntity(response, listRoleRepresentationTypeReference);
            } else {
                throw new KeycloakClientException("Failed to post composite", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak post composite by Role ID failed", e);
        }
        return null;
    }

    //
    // delete composite
    //

    public void deleteComposite(String roleName, List<RoleRepresentation> composites) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        deleteComposite(getRealm(), roleName, composites);
    }

    public void deleteComposite(String realm, String roleName, List<RoleRepresentation> composites) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doDelete(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/roles/" + roleName + "/composites", composites);
            if(!isHttpOkStatus(response)) {
                throw new KeycloakClientException("Failed to delete composite", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak delete composite failed", e);
        }
    }

    //
    // delete composite by Role Id
    //

    public void deleteCompositeByRoleId(String roleId, List<RoleRepresentation> composites) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();
        deleteCompositeByRoleId(getRealm(), roleId, composites);
    }

    public void deleteCompositeByRoleId(String realm, String roleId, List<RoleRepresentation> composites) throws KeycloakClientException {
        assertKeycloakAdminClientInitialised();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doDelete(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/roles-by-id/" + roleId.trim() + "/composites", composites);
            if(!isHttpOkStatus(response)) {
                throw new KeycloakClientException("Failed to delete composite", response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            LOG.error("Keycloak delete composite by Role ID failed", e);
        }
    }
}
