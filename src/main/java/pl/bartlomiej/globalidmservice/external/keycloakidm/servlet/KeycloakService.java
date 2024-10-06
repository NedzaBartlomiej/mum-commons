package pl.bartlomiej.globalidmservice.external.keycloakidm.servlet;

import pl.bartlomiej.globalidmservice.external.keycloakidm.model.KeycloakRole;
import pl.bartlomiej.globalidmservice.external.keycloakidm.model.KeycloakUserRegistration;
import pl.bartlomiej.globalidmservice.external.keycloakidm.model.KeycloakUserRepresentation;

public interface KeycloakService {
    KeycloakUserRepresentation create(KeycloakUserRegistration keycloakUserRegistration);

    void delete(String id);

    void assignRole(String id, KeycloakRole keycloakRole);
}
