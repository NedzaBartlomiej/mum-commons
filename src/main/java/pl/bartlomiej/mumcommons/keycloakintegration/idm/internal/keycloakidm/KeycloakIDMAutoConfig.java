package pl.bartlomiej.mumcommons.keycloakintegration.idm.internal.keycloakidm;

import org.keycloak.admin.client.Keycloak;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import pl.bartlomiej.mumcommons.keycloakintegration.idm.external.keycloakidm.KeycloakService;

@AutoConfiguration
@ConditionalOnBean(Keycloak.class)
class KeycloakIDMAutoConfig {

    @Bean
    @ConditionalOnProperty(value = "mum-commons.keycloak.idm.enabled", havingValue = "true")
    KeycloakService keycloakService(KeycloakProperties properties, Keycloak keycloak) {
        return new DefaultKeycloakService(properties, keycloak);
    }
}