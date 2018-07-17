package org.endeavourhealth.common.security.keycloak.client.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.endeavourhealth.common.cache.ObjectMapperPool;
import org.endeavourhealth.common.security.KeycloakConfigUtils;
import org.endeavourhealth.common.security.keycloak.client.KeycloakClient;
import org.keycloak.adapters.KeycloakDeployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class KeycloakAdminClientBase {

    protected static final Logger LOG = LoggerFactory.getLogger(KeycloakAdminClientBase.class);

    private KeycloakDeployment keycloakDeployment;
    private String authBaseUrl;

    protected boolean initKeycloakAdmin = false;

    protected void initKeycloakAdminClient() {

        // get the Endeavour realm name
        keycloakDeployment = KeycloakConfigUtils.getDeployment("keycloak");
        authBaseUrl = KeycloakConfigUtils.initialize();

        try {
            LOG.trace("Keycloak token = '{}'", KeycloakClient.instance().getToken().getToken());
        } catch (IOException e) {
            LOG.trace("Keycloak token = 'null'");
        }

        initKeycloakAdmin = true;
    }

    protected void assertKeycloakAdminClientInitialised() {
        if(!initKeycloakAdmin) {
            initKeycloakAdminClient();
        }
    }

    protected String getRealm() {
        return keycloakDeployment.getRealm();
    }

    protected String getAuthServerBaseUrl() {
        return authBaseUrl;
    }

    //
    // Helper methods
    //

    protected HttpResponse doGet(CloseableHttpClient httpClient, String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader(KeycloakClient.instance().getAuthorizationHeader());
        return httpClient.execute(httpGet);
    }

    protected HttpResponse doPost(CloseableHttpClient httpClient, String url, Object entity) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(KeycloakClient.instance().getAuthorizationHeader());
        String content = ObjectMapperPool.getInstance().writeValueAsString(entity);
        HttpEntity httpEntity = new StringEntity(content, ContentType.APPLICATION_JSON);
        httpPost.setEntity(httpEntity);
        return httpClient.execute(httpPost);
    }

    protected HttpResponse doPut(CloseableHttpClient httpClient, String url, Object entity) throws IOException {
        HttpPut httpPut = new HttpPut(url);
        httpPut.addHeader(KeycloakClient.instance().getAuthorizationHeader());
        String content = ObjectMapperPool.getInstance().writeValueAsString(entity);
        HttpEntity httpEntity = new StringEntity(content, ContentType.APPLICATION_JSON);
        httpPut.setEntity(httpEntity);
        return httpClient.execute(httpPut);
    }

    protected HttpResponse doDelete(CloseableHttpClient httpClient, String url) throws IOException {
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.addHeader(KeycloakClient.instance().getAuthorizationHeader());
        return httpClient.execute(httpDelete);
    }

    protected HttpResponse doDelete(CloseableHttpClient httpClient, String url, Object entity) throws IOException {
        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
        httpDelete.addHeader(KeycloakClient.instance().getAuthorizationHeader());
        String content = ObjectMapperPool.getInstance().writeValueAsString(entity);
        HttpEntity httpEntity = new StringEntity(content, ContentType.APPLICATION_JSON);
        httpDelete.setEntity(httpEntity);
        return httpClient.execute(httpDelete);
    }

    protected <T> T toEntity(HttpResponse httpResponse, TypeReference<T> typeReference) throws IOException {
        return ObjectMapperPool.getInstance().readValue(httpResponse.getEntity().getContent(), typeReference);
    }

    protected String getIdFromLocation(HttpResponse httpResponse) {
        Header locationHeader = httpResponse.getFirstHeader(HttpHeaders.LOCATION);
        if(locationHeader == null || StringUtils.isBlank(locationHeader.getValue())) {
            return null;
        }

        return locationHeader.getValue().substring(locationHeader.getValue().lastIndexOf("/") + 1);
    }

    protected boolean isHttpOkStatus(HttpResponse response) {
        return (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300);
    }

}
