package pl.bartlomiej.mummicroservicecommons.authconversion.internal;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public abstract class AbstractJwtGrantedAuthoritiesConverter {

    private final String keycloakClientId;
    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String RESOURCE_ACCESS_CLAIM = "resource_access";
    private static final String ROLES_CLAIM = "roles";

    protected AbstractJwtGrantedAuthoritiesConverter(String keycloakClientId) {
        this.keycloakClientId = keycloakClientId;
    }

    protected List<String> extractRoles(final Jwt source) {
        return Stream.concat(
                this.extractRealmRoles(source),
                this.extractClientRoles(source)
        ).toList();
    }

    private Stream<String> extractRealmRoles(final Jwt source) {
        Map<String, Object> realmAccess = source.getClaim(REALM_ACCESS_CLAIM);
        Object roles = realmAccess.get(ROLES_CLAIM);
        return this.map(roles);
    }

    private Stream<String> extractClientRoles(final Jwt source) {
        Map<String, Object> resourceAccess = source.getClaim(RESOURCE_ACCESS_CLAIM);
        Object roles;
        if (resourceAccess.get(keycloakClientId) instanceof Map<?, ?> client) {
            roles = client.get(ROLES_CLAIM);
            return this.map(roles);
        }
        return Stream.empty();
    }

    private Stream<String> map(final Object roles) {
        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .filter(role -> role instanceof String)
                    .map(role -> (String) role);
        }
        return Stream.empty();
    }
}