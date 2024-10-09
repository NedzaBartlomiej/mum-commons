package pl.bartlomiej.mummicroservicecommons.authconversion.internal;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import pl.bartlomiej.mummicroservicecommons.authconversion.external.reactor.KeycloakReactiveJwtGrantedAuthoritiesConverter;
import pl.bartlomiej.mummicroservicecommons.authconversion.external.servlet.KeycloakJwtGrantedAuthoritiesConverter;

@AutoConfiguration
class AuthConversionAutoConfig {

    @Bean
    @ConditionalOnProperty(value = "mum-microservice-commons.auth-conversion.type", havingValue = "servlet")
    KeycloakJwtGrantedAuthoritiesConverter keycloakJwtGrantedAuthoritiesConverter() {
        return new KeycloakJwtGrantedAuthoritiesConverter();
    }

    @Bean
    @ConditionalOnProperty(value = "mum-microservice-commons.auth-conversion.type", havingValue = "reactor")
    KeycloakReactiveJwtGrantedAuthoritiesConverter keycloakReactiveJwtGrantedAuthoritiesConverter() {
        return new KeycloakReactiveJwtGrantedAuthoritiesConverter();
    }
}