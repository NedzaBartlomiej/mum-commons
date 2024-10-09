package pl.bartlomiej.mummicroservicecommons.authconversion.internal;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import pl.bartlomiej.mummicroservicecommons.authconversion.external.reactor.KeycloakReactiveJwtGrantedAuthoritiesConverter;
import pl.bartlomiej.mummicroservicecommons.authconversion.external.servlet.KeycloakJwtGrantedAuthoritiesConverter;

@AutoConfiguration
class AuthConversionAutoConfig {

    @Bean
    @ConditionalOnProperty(value = "jwt-granted-authority-converter.type", havingValue = "servlet")
    KeycloakJwtGrantedAuthoritiesConverter keycloakJwtGrantedAuthoritiesConverter() {
        return new KeycloakJwtGrantedAuthoritiesConverter();
    }

    @Bean
    @ConditionalOnProperty(value = "jwt-granted-authority-converter.type", havingValue = "reactor")
    KeycloakReactiveJwtGrantedAuthoritiesConverter keycloakReactiveJwtGrantedAuthoritiesConverter() {
        return new KeycloakReactiveJwtGrantedAuthoritiesConverter();
    }
}