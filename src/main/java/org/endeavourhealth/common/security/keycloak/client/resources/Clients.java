package org.endeavourhealth.common.security.keycloak.client.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Clients extends KeycloakAdminClientBase {

    private static final TypeReference<ClientRepresentation> clientRepresentationTypeReference = new TypeReference<ClientRepresentation>() {};
    private static final TypeReference<List<ClientRepresentation>> listClientRepresentationTypeReference = new TypeReference<List<ClientRepresentation>>() {};
    private static final TypeReference<List<RoleRepresentation>> listRoleRepresentationTypeReference = new TypeReference<List<RoleRepresentation>>() {};

    //
    // get clients
    //
    public List<ClientRepresentation> getRealmClients() {
        assertKeycloakAdminClientInitialised();
        return getRealmClients(getRealm());
    }

    public List<ClientRepresentation> getRealmClients(String realm) {
        assertKeycloakAdminClientInitialised();

        List<ClientRepresentation> clients = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/clients");
            clients = toEntity(response, listClientRepresentationTypeReference);
        } catch (IOException e) {
            LOG.error("Keycloak get clients failed", e);
        }
        return clients;
    }

    //
    // get client roles
    //
    public List<RoleRepresentation> getClientRoles(String clientId) {
        assertKeycloakAdminClientInitialised();
        return getClientRoles(getRealm(), clientId);
    }

    public List<RoleRepresentation> getClientRoles(String realm, String clientId) {
        assertKeycloakAdminClientInitialised();

        List<RoleRepresentation> clients = null;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doGet(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realm + "/clients/"+clientId.trim()+"/roles");
            clients = toEntity(response, listRoleRepresentationTypeReference);
        } catch (IOException e) {
            LOG.error("Keycloak get clients failed", e);
        }
        return clients;
    }

    //
    // create client
    //
    public ClientRepresentation createClient(String realmId, String clientName) {
        return createClient(realmId, clientName, null, null);
    }

    public ClientRepresentation createClient(String realmId, String clientName, List<String> redirectUris, List<String> webOrigins) {

        assertKeycloakAdminClientInitialised();

        ClientRepresentation client = getDefaultClientRepresentation(clientName,
                redirectUris != null ? redirectUris : Arrays.asList("*"),
                webOrigins != null ? webOrigins : Arrays.asList("*"));

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = doPost(httpClient, getAuthServerBaseUrl() + "/admin/realms/" + realmId + "/clients", client);
            client = toEntity(response, clientRepresentationTypeReference);
        } catch (IOException e) {
            LOG.error("Keycloak post client failed", e);
        }
        return client;
    }

    private ClientRepresentation getDefaultClientRepresentation(String clientName, List<String> redirectUris, List<String> webOrigins) {
        ClientRepresentation clientRepresentation;
        clientRepresentation = new ClientRepresentation();
        clientRepresentation.setId(clientName);
        clientRepresentation.setName(clientName);
        clientRepresentation.setPublicClient(true);
        clientRepresentation.setDirectAccessGrantsEnabled(true);
        clientRepresentation.setRedirectUris(redirectUris);
        clientRepresentation.setWebOrigins(webOrigins);
        return clientRepresentation;
    }
}
