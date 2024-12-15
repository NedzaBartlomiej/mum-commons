package pl.bartlomiej.mumcommons.globalidmservice.idm.internal.keycloakidm;

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
import pl.bartlomiej.mumcommons.core.offsettransaction.servlet.OffsetTransactionOperator;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model.KeycloakRole;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model.KeycloakUserRegistration;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model.KeycloakUserRepresentation;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.servlet.KeycloakService;

import java.util.Collections;

class DefaultKeycloakService extends AbstractKeycloakService implements KeycloakService {

    private static final Logger log = LoggerFactory.getLogger(DefaultKeycloakService.class);
    private final RealmResource realmResource;
    private final Keycloak keycloak;
    private final KeycloakProperties properties;

    DefaultKeycloakService(KeycloakProperties properties, Keycloak keycloak) {
        this.keycloak = keycloak;
        this.realmResource = keycloak.realm(properties.realmName());
        this.properties = properties;
    }

    @Override
    public KeycloakUserRepresentation create(final KeycloakUserRegistration keycloakUserRegistration) {
        log.info("Started keycloak user creation process.");

        UserRepresentation userRepresentation = buildUserRepresentation(keycloakUserRegistration);

        UsersResource usersResource = this.realmResource.users();

        log.info("Creating user.");
        KeycloakUserRepresentation createdUser;
        try (Response response = usersResource.create(userRepresentation)) {
            handleResponseStatus(response, HttpStatus.CREATED);

            createdUser = new KeycloakUserRepresentation(
                    CreatedResponseUtil.getCreatedId(response),
                    keycloakUserRegistration.getUsername(),
                    keycloakUserRegistration.getEmail()
            );
        }

        OffsetTransactionOperator.performOffsetConsumerTransaction(
                createdUser,
                createdUser.id(),
                u -> this.assignClientRole(u.id(), keycloakUserRegistration.getDefaultRole()),
                this::delete
        );

        log.info("Sending verification email message.");
        OffsetTransactionOperator.performOffsetConsumerTransaction(
                createdUser,
                createdUser.id(),
                u -> usersResource.get(u.id()).sendVerifyEmail(),
                this::delete
        );

        log.debug("Returning successfully created user.");
        return createdUser;
    }

    @Override
    public void delete(final String id) {
        log.info("Started deletion keycloak user process.");
        try (Response response = realmResource.users().delete(id)) {
            handleResponseStatus(response, HttpStatus.NO_CONTENT);
        }
        log.info("User has been deleted from the keycloak auth-server.");
    }

    @Override
    public void assignClientRole(final String id, final KeycloakRole keycloakRole) {
        log.info("Started user assigning role process.");
        UserResource userResource = realmResource.users().get(id);
        ClientRepresentation clientRepresentation = realmResource.clients().findByClientId(properties.clientId()).getFirst();
        RolesResource rolesResource = realmResource.clients().get(clientRepresentation.getId()).roles();

        log.debug("Getting equivalent keycloak role to argument role.");
        RoleRepresentation roleRepresentation = rolesResource.get(keycloakRole.getRole()).toRepresentation();

        log.info("Assigning keycloak role to an user.");
        userResource.roles().clientLevel(clientRepresentation.getId()).add(Collections.singletonList(roleRepresentation));
    }

    private static void handleResponseStatus(final Response response, final HttpStatus successStatus) {
        if (response.getStatus() != successStatus.value()) {
            log.error("Some error status occurred in keycloak user creation process response: {}", response.getStatusInfo());
            throw new ErrorResponseException(HttpStatus.valueOf(response.getStatus()));
        }
    }
}