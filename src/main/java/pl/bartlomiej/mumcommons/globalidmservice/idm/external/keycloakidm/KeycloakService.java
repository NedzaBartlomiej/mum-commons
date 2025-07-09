package pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm;

import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model.KeycloakRole;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model.KeycloakUserRegistration;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model.KeycloakUserRepresentation;

public interface KeycloakService {
    KeycloakUserRepresentation create(KeycloakUserRegistration keycloakUserRegistration);

    void delete(String id);

    void assignClientRole(String id, KeycloakRole keycloakRole);
}
