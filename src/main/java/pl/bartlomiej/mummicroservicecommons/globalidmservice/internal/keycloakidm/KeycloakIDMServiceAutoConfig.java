package pl.bartlomiej.mummicroservicecommons.globalidmservice.internal.keycloakidm;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import pl.bartlomiej.mummicroservicecommons.globalidmservice.external.keycloakidm.reactor.ReactiveKeycloakService;
import pl.bartlomiej.mummicroservicecommons.globalidmservice.external.keycloakidm.servlet.KeycloakService;

@AutoConfiguration
@EnableConfigurationProperties(KeycloakIDMServiceProperties.class)
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
    @ConditionalOnProperty(value = "mum-microservice-commons.global-idm-service.type", havingValue = "servlet")
    public KeycloakService keycloakService(KeycloakIDMServiceProperties properties, Keycloak keycloak) {
        return new DefaultKeycloakService(properties, keycloak);
    }

    @Bean
    @ConditionalOnProperty(value = "mum-microservice-commons.global-idm-service.type", havingValue = "reactor")
    public ReactiveKeycloakService reactiveKeycloakService(KeycloakIDMServiceProperties properties, Keycloak keycloak) {
        return new DefaultReactiveKeycloakService(properties, keycloak);
    }
}
