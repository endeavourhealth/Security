package org.endeavourhealth.common.security.keycloak;

import org.endeavourhealth.common.security.KeycloakConfigUtils;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.OIDCHttpFacade;
import org.keycloak.adapters.spi.HttpFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Random;

/**
 * Provides the keycloak configuration file to the Tomcat adapter.
 * <p/>
 * Multi-tenant and special config can be injected here.
 */
public class KeycloakConfigResolverImpl implements KeycloakConfigResolver {

    private static final Logger LOG = LoggerFactory.getLogger(KeycloakConfigResolverImpl.class);

    public KeycloakConfigResolverImpl() {
        LOG.debug("created debug");
    }

    @Override
    public KeycloakDeployment resolve(OIDCHttpFacade.Request request) {

        //depending on the first element of the path, we use either the config for our
        //regular user realm or our machine realm (for server-to-server comms)
        String keycloakConfigToUse = "keycloak";

        String uri = request.getURI();
        try {
            String path = new URL(uri).getPath();
            String[] toks = path.split("/");
            for (String tok: toks) {
                if (tok.equalsIgnoreCase("machine-api")) {
                    keycloakConfigToUse = "keycloak_machine";
                    break;
                }
            }

        } catch (Exception ex) {
            LOG.error("Failed to parse path from " + uri + " will use default keycloak config [" + keycloakConfigToUse + "]");
        }

        LOG.debug("Using config " + keycloakConfigToUse);
        return KeycloakConfigUtils.getDeployment(keycloakConfigToUse);

        /*KeycloakDeployment keycloakDeployment = KeycloakConfigUtils.getDeployment("keycloak");

        if (keycloakDeployment == null) {
            LOG.warn("Cannot get Keycloak config from configuration repository, falling back to fixed internal configuration.");
            keycloakDeployment = KeycloakDeploymentBuilder.build(getClass().getResourceAsStream("/keycloak.json"));
        }
        return keycloakDeployment;*/
    }

}