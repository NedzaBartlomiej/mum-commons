package pl.bartlomiej.mummicroservicecommons.authconversion.internal;

import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractJwtGrantedAuthoritiesConverter {
    protected List<String> extractRoles(@NonNull final Jwt source) {
        final String realmAccessClaim = "realm_access";
        final String rolesClaim = "roles";
        Map<String, Object> realmAccess = source.getClaim(realmAccessClaim);
        Object roles = realmAccess.get(rolesClaim);
        if (roles instanceof List<?>) {
            return ((List<?>) roles).stream()
                    .filter(role -> role instanceof String)
                    .map(role -> (String) role)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}