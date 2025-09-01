package pl.bartlomiej.mumcommons.keycloakintegration.idm.internal.keycloakidm;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.bartlomiej.mumcommons.keycloakintegration.idm.external.keycloakidm.model.KeycloakUserRegistration;

import java.util.Collections;

public abstract class AbstractKeycloakService {
    private static final Logger log = LoggerFactory.getLogger(AbstractKeycloakService.class);

    protected UserRepresentation buildUserRepresentation(final KeycloakUserRegistration keycloakUserRegistration) {
        log.debug("Building UserRepresentation for the user being created.");
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(keycloakUserRegistration.getUsername());
        userRepresentation.setEmail(keycloakUserRegistration.getEmail());
        userRepresentation.setEmailVerified(false);
        userRepresentation.setRequiredActions(Collections.singletonList("VERIFY_EMAIL"));

        CredentialRepresentation credentialRepresentation = buildCredentialRepresentation(keycloakUserRegistration);

        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        return userRepresentation;
    }

    private CredentialRepresentation buildCredentialRepresentation(final KeycloakUserRegistration keycloakUserRegistration) {
        log.debug("Building CredentialRepresentation for the user being created.");
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(keycloakUserRegistration.getPassword());
        return credentialRepresentation;
    }
}
