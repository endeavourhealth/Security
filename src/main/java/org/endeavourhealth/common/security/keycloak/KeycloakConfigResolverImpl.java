package org.endeavourhealth.common.security.keycloak;

import org.endeavourhealth.common.security.KeycloakConfigUtils;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.OIDCHttpFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides the keycloak configuration file to the Tomcat adapter.
 *
 * Multi-tenant and special config can be injected here.
 */
public class KeycloakConfigResolverImpl implements KeycloakConfigResolver {

    private static final Logger LOG = LoggerFactory.getLogger(KeycloakConfigResolverImpl.class);

    @Override
    public KeycloakDeployment resolve(OIDCHttpFacade.Request request) {
					KeycloakDeployment keycloakDeployment = KeycloakConfigUtils.getDeployment();

					if(keycloakDeployment == null) {
							LOG.warn("Cannot get Keycloak config from configuration repository, falling back to fixed internal configuration.");
							keycloakDeployment = KeycloakDeploymentBuilder.build(getClass().getResourceAsStream("/keycloak.json"));
					}
        return keycloakDeployment;
    }

}