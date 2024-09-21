package pl.bartlomiej.keycloakidmservice.internal;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak-idm-service")
record KeycloakIDMServiceProperties(
        Boolean enabled,
        String serverUrl,
        String realmName,
        String clientId,
        String clientSecret
) {
}