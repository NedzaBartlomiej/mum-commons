package pl.bartlomiej.globalidmservice.external.keycloakidm.model;

public interface KeycloakUserRegistration {
    String getUsername();

    String getPassword();

    KeycloakRole getDefaultRole();

    String getEmail();
}
