package org.endeavourhealth.common.security;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.common.cache.ObjectMapperPool;
import org.endeavourhealth.common.config.ConfigManager;
import org.endeavourhealth.common.security.keycloak.client.KeycloakClient;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class KeycloakConfigUtils {

    private static final Logger LOG = LoggerFactory.getLogger(KeycloakConfigUtils.class);

    public static KeycloakDeployment getDeployment() {
        String keycloakConfigJson = ConfigManager.getConfiguration("keycloak");

        if (keycloakConfigJson != null && StringUtils.isNotEmpty(keycloakConfigJson)) {
            InputStream stream = new ByteArrayInputStream(keycloakConfigJson.getBytes(StandardCharsets.UTF_8));
            return KeycloakDeploymentBuilder.build(stream);
        }

        return null;
    }

    public static String initialize() {
        String keycloakProxyJson = ConfigManager.getConfiguration("keycloak_proxy");
        String adminClientUrl = null;

        try {
            JsonNode keycloakProxy = ObjectMapperPool.getInstance().readTree(keycloakProxyJson);
            adminClientUrl = keycloakProxy.get("url").asText();
            String adminClientUsername = keycloakProxy.get("user").asText();
            String adminClientPassword = keycloakProxy.get("pass").asText();

            // build the admin client
            KeycloakClient.init(adminClientUrl,
                "master",
                adminClientUsername,
                adminClientPassword,
                "admin-cli");
        } catch (IOException e) {
            LOG.error("Unable to parse keycloak proxy data");;
        }

        return adminClientUrl;
    }
}
