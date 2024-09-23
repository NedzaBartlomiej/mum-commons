package pl.bartlomiej.keycloakidmservice.internal;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import pl.bartlomiej.keycloakidmservice.external.servlet.KeycloakService;
import pl.bartlomiej.keycloakidmservice.external.exception.KeycloakResponseException;
import pl.bartlomiej.keycloakidmservice.external.model.KeycloakRole;
import pl.bartlomiej.keycloakidmservice.external.model.KeycloakUserRegistration;
import pl.bartlomiej.keycloakidmservice.external.model.KeycloakUserRepresentation;
import pl.bartlomiej.offsettransaction.servlet.OffsetTransactionOperator;

import java.util.Collections;

class DefaultKeycloakService implements KeycloakService {

    private static final Logger log = LoggerFactory.getLogger(DefaultKeycloakService.class);
    private final RealmResource realmResource;

    DefaultKeycloakService(KeycloakIDMServiceProperties properties, Keycloak keycloak) {
        this.realmResource = keycloak.realm(properties.realmName());
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

            String createdId = CreatedResponseUtil.getCreatedId(response);

            createdUser = new KeycloakUserRepresentation(
                    createdId,
                    keycloakUserRegistration.getUsername()
            );
        }

        OffsetTransactionOperator.performOffsetConsumerTransaction(
                createdUser,
                createdUser.id(),
                u -> this.assignRole(u.id(), keycloakUserRegistration.getDefaultRole()),
                this::delete
        );

        log.info("Sending verification email message.");
        usersResource.get(createdUser.id()).sendVerifyEmail();

        log.debug("Returning created user successfully.");
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
    public void assignRole(final String id, final KeycloakRole keycloakRole) {
        log.info("Started user assigning role process.");
        UserResource userResource = realmResource.users().get(id);
        RolesResource rolesResource = realmResource.roles();

        log.debug("Getting equivalent keycloak role to argument role.");
        RoleRepresentation roleRepresentation = rolesResource.get(keycloakRole.getRole()).toRepresentation();

        log.info("Assigning role for the admin.");
        userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));
    }

    private static void handleResponseStatus(final Response response, final HttpStatus successStatus) {
        if (response.getStatus() != successStatus.value()) {
            log.error("Some error status occurred in keycloak user creation process response: {}" +
                    "Forwarding exception to the RestControllerAdvice", response.getStatusInfo());
            throw new KeycloakResponseException(HttpStatus.valueOf(response.getStatus()));
        }
    }

    private static UserRepresentation buildUserRepresentation(final KeycloakUserRegistration keycloakUserRegistration) {
        log.debug("Building UserRepresentation for the user being created.");
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(keycloakUserRegistration.getUsername());
        userRepresentation.setEmail("bartek21122006@gmail.com");
        userRepresentation.setEmailVerified(false);
        userRepresentation.setRequiredActions(Collections.singletonList("VERIFY_EMAIL"));

        CredentialRepresentation credentialRepresentation = buildCredentialRepresentation(keycloakUserRegistration);

        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        return userRepresentation;
    }

    private static CredentialRepresentation buildCredentialRepresentation(final KeycloakUserRegistration keycloakUserRegistration) {
        log.debug("Building CredentialRepresentation for the user being created.");
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(keycloakUserRegistration.getPassword());
        return credentialRepresentation;
    }
}