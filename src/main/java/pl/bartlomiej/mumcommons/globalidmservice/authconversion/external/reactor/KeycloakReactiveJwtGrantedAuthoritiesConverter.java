package pl.bartlomiej.mumcommons.globalidmservice.authconversion.external.reactor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import pl.bartlomiej.mumcommons.globalidmservice.authconversion.external.model.UserRoleAuthority;
import pl.bartlomiej.mumcommons.globalidmservice.authconversion.internal.AbstractJwtGrantedAuthoritiesConverter;
import reactor.core.publisher.Flux;

public class KeycloakReactiveJwtGrantedAuthoritiesConverter extends AbstractJwtGrantedAuthoritiesConverter implements Converter<Jwt, Flux<GrantedAuthority>> {

    public KeycloakReactiveJwtGrantedAuthoritiesConverter(String keycloakClientId) {
        super(keycloakClientId);
    }

    @Override
    public Flux<GrantedAuthority> convert(Jwt source) {
        return Flux.fromIterable(super.extractRoles(source))
                .map(UserRoleAuthority::new);
    }
}
