package pl.bartlomiej.mumcommons.globalidmservice.idm.internal.keycloakidm;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.KeycloakService;

@AutoConfiguration
@EnableConfigurationProperties(KeycloakProperties.class)
@ConditionalOnProperty(value = "mum-commons.keycloak.enabled", havingValue = "true")
class KeycloakIDMServiceAutoConfig {

    @Bean
    Keycloak keycloakClient(KeycloakProperties properties) {
        return KeycloakBuilder.builder()
                .serverUrl(properties.serverUrl())
                .realm(properties.realmName())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(properties.clientId())
                .clientSecret(properties.clientSecret())
                .build();
    }

    @Bean
    @ConditionalOnProperty(value = "mum-commons.keycloak.global-idm-service.enabled", havingValue = "true")
    public KeycloakService keycloakService(KeycloakProperties properties, Keycloak keycloak) {
        return new DefaultKeycloakService(properties, keycloak);
    }
}