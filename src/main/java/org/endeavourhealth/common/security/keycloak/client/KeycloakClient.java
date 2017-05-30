package org.endeavourhealth.common.security.keycloak.client;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.keycloak.OAuth2Constants;
import org.keycloak.common.util.KeycloakUriBuilder;
import org.keycloak.constants.ServiceUrlConstants;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.util.JsonSerialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class KeycloakClient {

    protected static final Logger LOG = LoggerFactory.getLogger(KeycloakClient.class);

    private static KeycloakClient instance;
    private String baseUrl;
    private String realm;
    private String username;
    private String password;
    private String clientId;
    private AccessTokenResponse currentToken;
    private Date expirationTime;
    private Date refreshTokenExpirationTime;

    public KeycloakClient(String baseUrl, String realm, String username, String password, String clientId) {
        this.baseUrl = baseUrl;
        this.realm = realm;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
    }

    public static void init(String baseUrl, String realm, String username, String password, String clientId) {
        instance = new KeycloakClient(baseUrl, realm, username, password, clientId);
    }

    public static KeycloakClient instance() {
        return instance;
    }

    public AccessTokenResponse getToken() throws IOException {
        if(currentToken == null || refreshTokenExpired()) {
            LOG.trace("No token set or refresh token has expired, getting a new one...");
            currentToken = getTokenInternal();
            setExpirationTime();
            setRefreshExpirationTime();
        } else {
            if(tokenExpired()) {
                LOG.trace("Token has expired, refreshing now...");
                currentToken = refreshToken();
                setExpirationTime();
            }
        }
        return currentToken;
    }

    private void setExpirationTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, (int) currentToken.getExpiresIn());
        expirationTime = cal.getTime();
        if(LOG.isTraceEnabled()) {
            LOG.trace("Access token expires: '{}'", expirationTime.toString());
        }
    }

    private void setRefreshExpirationTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, (int) this.currentToken.getRefreshExpiresIn());
        refreshTokenExpirationTime = cal.getTime();
        if(LOG.isTraceEnabled()) {
            LOG.trace("Refresh token expires: '{}'", refreshTokenExpirationTime.toString());
        }
    }

    private boolean refreshTokenExpired() {
        return new Date().after(refreshTokenExpirationTime);
    }

    private boolean tokenExpired() {
        return new Date().after(expirationTime);
    }

    public Header getAuthorizationHeader() throws IOException {
        String token = getToken().getToken();
        return new BasicHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }

    AccessTokenResponse getTokenInternal() throws IOException {

        CloseableHttpClient client = HttpClientBuilder.create().build();

        try {
            LOG.debug("Building keycloak connection from base : ["+baseUrl+"], path : ["+ServiceUrlConstants.TOKEN_PATH+"], realm : ["+realm+"]");
            String url = KeycloakUriBuilder.fromUri(baseUrl).path(ServiceUrlConstants.TOKEN_PATH).build(realm).toString();
            LOG.debug("Built url : ["+url+"]");
            HttpPost post = new HttpPost(url);
            List<NameValuePair> formparams = new ArrayList<>();
            formparams.add(new BasicNameValuePair("username", username));
            formparams.add(new BasicNameValuePair("password", password));
            formparams.add(new BasicNameValuePair(OAuth2Constants.GRANT_TYPE, "password"));
            formparams.add(new BasicNameValuePair(OAuth2Constants.CLIENT_ID, clientId));
            UrlEncodedFormEntity form = new UrlEncodedFormEntity(formparams, "UTF-8");
            post.setEntity(form);

            LOG.debug("POST URL reporting : ["+post.getURI().toString()+"]");

            HttpResponse response = client.execute(post);
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (status != 200) {
                String json = getContent(entity);
                LOG.trace("Failed to log in: '{}'", json);
                throw new IOException("Bad status: " + status + " response: " + json);
            }
            if (entity == null) {
                LOG.trace("Failed to log in, no entity");
                throw new IOException("No Entity");
            }
            String json = getContent(entity);
            return JsonSerialization.readValue(json, AccessTokenResponse.class);
        } finally {
            client.close();
        }
    }

    AccessTokenResponse refreshToken() throws IOException {

        CloseableHttpClient client = HttpClients.createDefault();

        try {
            HttpPost post = new HttpPost(KeycloakUriBuilder.fromUri(baseUrl)
                    .path(ServiceUrlConstants.TOKEN_PATH).build(realm));
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair(OAuth2Constants.GRANT_TYPE, "refresh_token"));
            formparams.add(new BasicNameValuePair(OAuth2Constants.CLIENT_ID, clientId));
            formparams.add(new BasicNameValuePair(OAuth2Constants.REFRESH_TOKEN, currentToken.getRefreshToken()));
            UrlEncodedFormEntity form = new UrlEncodedFormEntity(formparams, "UTF-8");
            post.setEntity(form);

            HttpResponse response = client.execute(post);
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (status != 200) {
                String json = getContent(entity);
                throw new IOException("Bad status: " + status + " response: " + json);
            }
            if (entity == null) {
                throw new IOException("No Entity");
            }
            String json = getContent(entity);
            return JsonSerialization.readValue(json, AccessTokenResponse.class);
        } finally {
            client.close();
        }
    }

    public String getContent(HttpEntity entity) throws IOException {
        if (entity == null) return null;
        InputStream is = entity.getContent();
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int c;
            while ((c = is.read()) != -1) {
                os.write(c);
            }
            byte[] bytes = os.toByteArray();
            String data = new String(bytes);
            return data;
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {

            }
        }
    }

    public UserRepresentation getUserAccount() throws IOException {

        CloseableHttpClient client = HttpClients.createDefault();

        try {
            HttpGet get = new HttpGet(KeycloakUriBuilder.fromUri(baseUrl)
                    .path(ServiceUrlConstants.ACCOUNT_SERVICE_PATH).build(realm));

            System.out.println(get.getURI().toString());

            get.setHeader(getAuthorizationHeader());
            get.setHeader(HttpHeaders.ACCEPT, "application/json");

            HttpResponse response = client.execute(get);
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (status != 200) {
                String json = getContent(entity);
                throw new IOException("Bad status: " + status + " response: " + json);
            }
            if (entity == null) {
                throw new IOException("No Entity");
            }
            String json = getContent(entity);
            return JsonSerialization.readValue(json, UserRepresentation.class);
        } finally {
            client.close();
        }
    }


}
