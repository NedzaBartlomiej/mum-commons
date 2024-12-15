package pl.bartlomiej.mumcommons.globalidmservice.authconversion.external.servlet;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import pl.bartlomiej.mumcommons.globalidmservice.authconversion.external.model.UserRoleAuthority;
import pl.bartlomiej.mumcommons.globalidmservice.authconversion.internal.AbstractJwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.stream.Collectors;

public class KeycloakJwtGrantedAuthoritiesConverter extends AbstractJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    public KeycloakJwtGrantedAuthoritiesConverter(String keycloakClientId) {
        super(keycloakClientId);
    }

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt source) {
        return super.extractRoles(source).stream()
                .map(UserRoleAuthority::new)
                .collect(Collectors.toList());
    }
}