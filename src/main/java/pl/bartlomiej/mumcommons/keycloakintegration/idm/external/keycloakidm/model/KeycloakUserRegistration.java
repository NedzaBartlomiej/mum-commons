package pl.bartlomiej.mumcommons.keycloakintegration.idm.external.keycloakidm.model;

public interface KeycloakUserRegistration {
    String getUsername();

    String getPassword();

    /**
     * returns default role assigned to specific idm-service-representation,
     * for example, if idm-service-representation is api-service then its default role is "API-USER"
     */
    KeycloakRole getDefaultRole();

    String getEmail();
}
