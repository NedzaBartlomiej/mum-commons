package pl.bartlomiej.mumcommons.keycloakintegration.idm.external.keycloakidm;

import pl.bartlomiej.mumcommons.keycloakintegration.idm.external.keycloakidm.model.KeycloakRole;
import pl.bartlomiej.mumcommons.keycloakintegration.idm.external.keycloakidm.model.KeycloakUserRegistration;
import pl.bartlomiej.mumcommons.keycloakintegration.idm.external.keycloakidm.model.KeycloakUserRepresentation;

public interface KeycloakService {
    KeycloakUserRepresentation create(KeycloakUserRegistration keycloakUserRegistration);

    void delete(String id);

    void assignClientRole(String id, KeycloakRole keycloakRole);
}
