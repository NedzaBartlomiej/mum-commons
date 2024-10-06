package pl.bartlomiej.globalidmservice.internal.keycloakidm;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "global-idm-service.keycloak")
public record KeycloakIDMServiceProperties(
        String type,
        String serverUrl,
        String realmName,
        String clientId,
        String clientSecret
) {
}