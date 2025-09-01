package pl.bartlomiej.mumcommons.keycloakintegration.authconversion.external.model;

import org.springframework.security.core.GrantedAuthority;

public record UserRoleAuthority(String authority) implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return "ROLE_" + authority;
    }
}