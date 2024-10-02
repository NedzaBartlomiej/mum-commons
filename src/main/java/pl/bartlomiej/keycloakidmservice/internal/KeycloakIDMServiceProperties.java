package pl.bartlomiej.keycloakidmservice.internal;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak-idm-service")
public record KeycloakIDMServiceProperties(
        String type,
        String serverUrl,
        String realmName,
        String clientId,
        String clientSecret
) {
}