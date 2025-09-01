package pl.bartlomiej.mumcommons.keycloakintegration.idm.internal.keycloakidm;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mum-commons.keycloak")
public record KeycloakProperties(
        String type,
        String serverUrl,
        String realmName,
        String clientId,
        String clientSecret
) {
}