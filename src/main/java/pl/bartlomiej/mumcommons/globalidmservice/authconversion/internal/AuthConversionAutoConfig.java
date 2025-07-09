package pl.bartlomiej.mumcommons.globalidmservice.authconversion.internal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import pl.bartlomiej.mumcommons.globalidmservice.authconversion.external.KeycloakJwtGrantedAuthoritiesConverter;

@AutoConfiguration
@ConditionalOnProperty(value = "mum-commons.keycloak.auth-conversion.enabled", havingValue = "true")
class AuthConversionAutoConfig {

    @Bean
    KeycloakJwtGrantedAuthoritiesConverter keycloakJwtGrantedAuthoritiesConverter(@Value("${mum-commons.keycloak.auth-conversion.keycloak-client-id:#{null}}") final String keycloakClientId) {
        return new KeycloakJwtGrantedAuthoritiesConverter(keycloakClientId);
    }
}