package pl.bartlomiej.mumcommons.keycloakintegration.idm.internal.keycloakidm;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import pl.bartlomiej.mumcommons.coreutils.offsettransaction.OffsetTransactionOperator;
import pl.bartlomiej.mumcommons.keycloakintegration.idm.external.keycloakidm.KeycloakService;
import pl.bartlomiej.mumcommons.keycloakintegration.idm.external.keycloakidm.model.KeycloakRole;
import pl.bartlomiej.mumcommons.keycloakintegration.idm.external.keycloakidm.model.KeycloakUserRegistration;
import pl.bartlomiej.mumcommons.keycloakintegration.idm.external.keycloakidm.model.KeycloakUserRepresentation;

import java.util.Collections;

class DefaultKeycloakService extends AbstractKeycloakService implements KeycloakService {

    private static final Logger log = LoggerFactory.getLogger(DefaultKeycloakService.class);
    private final RealmResource realmResource;
    private final KeycloakProperties properties;

    DefaultKeycloakService(KeycloakProperties properties, Keycloak keycloak) {
        this.realmResource = keycloak.realm(properties.realmName());
        this.properties = properties;
    }

    @Override
    public KeycloakUserRepresentation create(final KeycloakUserRegistration keycloakUserRegistration) {

        log.info("Started keycloak user creation process.");
        UserRepresentation userRepresentation = super.buildUserRepresentation(keycloakUserRegistration);
        UsersResource usersResource = this.realmResource.users();

        log.info("Creating user.");
        KeycloakUserRepresentation createdUser;
        try (Response response = usersResource.create(userRepresentation)) {
            if (response.getStatus() == HttpStatus.CONFLICT.value()) {
                throw new ErrorResponseException(HttpStatus.valueOf(response.getStatus()));
            } else if (response.getStatus() != HttpStatus.CREATED.value()) {
                log.error("An error/unexpected responseStatus occurred during user creation process. Keycloak response status='{}'.", response.getStatus());
                throw new ErrorResponseException(HttpStatus.valueOf(response.getStatus()));
            }

            createdUser = new KeycloakUserRepresentation(
                    CreatedResponseUtil.getCreatedId(response),
                    keycloakUserRegistration.getUsername(),
                    keycloakUserRegistration.getEmail()
            );
        }

        OffsetTransactionOperator.performOffsetConsumerTransaction(
                createdUser.id(),
                createdUser.id(),
                id -> this.assignClientRole(id, keycloakUserRegistration.getDefaultRole()),
                this::delete
        );

        log.info("Sending verification email message.");
        OffsetTransactionOperator.performOffsetConsumerTransaction(
                createdUser.id(),
                createdUser.id(),
                id -> usersResource.get(id).sendVerifyEmail(),
                this::delete
        );

        log.debug("Returning successfully created user.");
        return createdUser;
    }

    @Override
    public void delete(final String id) {
        log.info("Started deletion keycloak user with id='{}' process.", id);
        try (Response response = realmResource.users().delete(id)) {
            if (response.getStatus() != HttpStatus.NO_CONTENT.value()) {
                log.error("An error/unexpected responseStatus occurred during deleting user with id='{}'; responseStatus='{}'", id, response.getStatus());
                throw new ErrorResponseException(HttpStatus.valueOf(response.getStatus()));
            }
        }
        log.info("User with id='{}' has been deleted from the keycloak auth-server.", id);
    }

    @Override
    public void assignClientRole(final String id, final KeycloakRole keycloakRole) {
        log.info("Started user with id='{}' assigning role process.", id);
        UserResource userResource = realmResource.users().get(id);
        ClientRepresentation clientRepresentation = realmResource.clients().findByClientId(properties.clientId()).getFirst();
        RolesResource rolesResource = realmResource.clients().get(clientRepresentation.getId()).roles();

        log.trace("Getting equivalent keycloak role to argument role for user with id='{}'.", id);
        RoleRepresentation roleRepresentation = rolesResource.get(keycloakRole.getRole()).toRepresentation();

        log.trace("Assigning keycloak role to an user with id='{}'.", id);
        userResource.roles().clientLevel(clientRepresentation.getId()).add(Collections.singletonList(roleRepresentation));
    }
}