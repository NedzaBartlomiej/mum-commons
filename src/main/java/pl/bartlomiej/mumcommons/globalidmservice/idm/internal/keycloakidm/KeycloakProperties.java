package pl.bartlomiej.mumcommons.globalidmservice.idm.internal.keycloakidm;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mum-commons.global-idm-service.keycloak")
public record KeycloakProperties(
        String type,
        String serverUrl,
        String realmName,
        String clientId,
        String clientSecret
) {
}