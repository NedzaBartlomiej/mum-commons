package pl.bartlomiej.keycloakidmservice.internal;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import pl.bartlomiej.keycloakidmservice.external.reactor.ReactiveKeycloakService;
import pl.bartlomiej.keycloakidmservice.external.servlet.KeycloakService;

@AutoConfiguration
@EnableConfigurationProperties(KeycloakIDMServiceProperties.class)
@ConditionalOnProperty(value = "keycloak-idm-service", havingValue = "true", matchIfMissing = true)
class KeycloakIDMServiceAutoConfig {

    @Bean
    Keycloak keycloakClient(KeycloakIDMServiceProperties properties) {
        return KeycloakBuilder.builder()
                .serverUrl(properties.serverUrl())
                .realm(properties.realmName())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(properties.clientId())
                .clientSecret(properties.clientSecret())
                .build();
    }

    @Bean
    public KeycloakService keycloakService(KeycloakIDMServiceProperties properties, Keycloak keycloak) {
        return new DefaultKeycloakService(properties, keycloak);
    }

    @Bean
    public ReactiveKeycloakService reactiveKeycloakService(KeycloakIDMServiceProperties properties, Keycloak keycloak) {
        return new DefaultReactiveKeycloakService(properties, keycloak);
    }
}
