package pl.bartlomiej.mumcommons.globalidmservice.authconversion.internal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import pl.bartlomiej.mumcommons.globalidmservice.authconversion.external.reactor.KeycloakReactiveJwtGrantedAuthoritiesConverter;
import pl.bartlomiej.mumcommons.globalidmservice.authconversion.external.servlet.KeycloakJwtGrantedAuthoritiesConverter;

@AutoConfiguration
class AuthConversionAutoConfig {

    @Bean
    @ConditionalOnProperty(value = "mum-commons.global-idm-service.type", havingValue = "servlet")
    KeycloakJwtGrantedAuthoritiesConverter keycloakJwtGrantedAuthoritiesConverter(@Value("${mum-commons.global-idm-service.auth-conversion.keycloak-client-id:#{null}}") final String keycloakClientId) {
        return new KeycloakJwtGrantedAuthoritiesConverter(keycloakClientId);
    }

    @Bean
    @ConditionalOnProperty(value = "mum-commons.global-idm-service.type", havingValue = "reactor")
    KeycloakReactiveJwtGrantedAuthoritiesConverter keycloakReactiveJwtGrantedAuthoritiesConverter(@Value("${mum-commons.global-idm-service.auth-conversion.keycloak-client-id:#{null}}") final String keycloakClientId) {
        return new KeycloakReactiveJwtGrantedAuthoritiesConverter(keycloakClientId);
    }
}