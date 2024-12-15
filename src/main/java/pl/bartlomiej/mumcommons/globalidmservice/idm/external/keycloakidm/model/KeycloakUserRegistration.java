package pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model;

public interface KeycloakUserRegistration {
    String getUsername();

    String getPassword();

    KeycloakRole getDefaultRole();

    String getEmail();
}
