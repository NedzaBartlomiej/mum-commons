package pl.bartlomiej.mummicroservicecommons.authconversion.internal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import pl.bartlomiej.mummicroservicecommons.authconversion.external.reactor.KeycloakReactiveJwtGrantedAuthoritiesConverter;
import pl.bartlomiej.mummicroservicecommons.authconversion.external.servlet.KeycloakJwtGrantedAuthoritiesConverter;

@AutoConfiguration
class AuthConversionAutoConfig {

    @Bean
    @ConditionalOnProperty(value = "mum-microservice-commons.auth-conversion.type", havingValue = "servlet")
    KeycloakJwtGrantedAuthoritiesConverter keycloakJwtGrantedAuthoritiesConverter(@Value("${mum-microservice-commons.auth-conversion.keycloak-client-id:#{null}}") final String keycloakClientId) {
        return new KeycloakJwtGrantedAuthoritiesConverter(keycloakClientId);
    }

    @Bean
    @ConditionalOnProperty(value = "mum-microservice-commons.auth-conversion.type", havingValue = "reactor")
    KeycloakReactiveJwtGrantedAuthoritiesConverter keycloakReactiveJwtGrantedAuthoritiesConverter(@Value("${mum-microservice-commons.auth-conversion.keycloak-client-id:#{null}}") final String keycloakClientId) {
        return new KeycloakReactiveJwtGrantedAuthoritiesConverter(keycloakClientId);
    }
}