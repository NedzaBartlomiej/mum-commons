package pl.bartlomiej.globalidmservice.internal.keycloakidm;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import pl.bartlomiej.globalidmservice.external.keycloakidm.model.KeycloakRole;
import pl.bartlomiej.globalidmservice.external.keycloakidm.model.KeycloakUserRegistration;
import pl.bartlomiej.globalidmservice.external.keycloakidm.model.KeycloakUserRepresentation;
import pl.bartlomiej.globalidmservice.external.keycloakidm.reactor.ReactiveKeycloakService;
import pl.bartlomiej.offsettransaction.reactor.ReactiveOffsetTransactionOperator;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;

public class DefaultReactiveKeycloakService extends AbstractKeycloakService implements ReactiveKeycloakService {
    private static final Logger log = LoggerFactory.getLogger(DefaultReactiveKeycloakService.class);
    private final RealmResource realmResource;

    DefaultReactiveKeycloakService(KeycloakIDMServiceProperties properties, Keycloak keycloak) {
        this.realmResource = keycloak.realm(properties.realmName());
    }


    @Override
    public Mono<KeycloakUserRepresentation> create(KeycloakUserRegistration keycloakUserRegistration) {
        log.info("Started keycloak user creation process.");

        UserRepresentation userRepresentation = AbstractKeycloakService.buildUserRepresentation(keycloakUserRegistration);

        UsersResource usersResource = this.realmResource.users();

        return this.createUser(keycloakUserRegistration, usersResource, userRepresentation)
                .flatMap(createdUser -> ReactiveOffsetTransactionOperator
                        .performOffsetConsumerTransaction(
                                createdUser,
                                createdUser.id(),
                                u -> this.assignRole(u.id(), keycloakUserRegistration.getDefaultRole()),
                                this::delete
                        ).thenReturn(createdUser)
                ).flatMap(createdUser -> this.sendVerificationEmail(usersResource, createdUser)
                ).doOnSuccess(unused -> log.debug("Returning successfully created user."));
    }

    private Mono<KeycloakUserRepresentation> createUser(KeycloakUserRegistration keycloakUserRegistration, UsersResource usersResource, UserRepresentation userRepresentation) {
        log.info("Creating user.");
        return Mono.fromCallable(() -> usersResource.create(userRepresentation))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(response -> handleResponseStatus(response, HttpStatus.CREATED)
                        .thenReturn(response)
                ).map(response -> new KeycloakUserRepresentation(
                        CreatedResponseUtil.getCreatedId(response),
                        keycloakUserRegistration.getUsername(),
                        keycloakUserRegistration.getEmail())
                );
    }

    private Mono<KeycloakUserRepresentation> sendVerificationEmail(UsersResource usersResource, KeycloakUserRepresentation createdUser) {
        log.info("Sending verification email message.");
        return ReactiveOffsetTransactionOperator.performOffsetFunctionTransaction(
                createdUser,
                createdUser.id(),
                u -> Mono.fromRunnable(() -> usersResource.get(u.id()).sendVerifyEmail())
                        .subscribeOn(Schedulers.boundedElastic())
                        .thenReturn(u),
                this::delete
        );
    }

    @Override
    public Mono<Void> delete(String id) {
        log.info("Started deletion keycloak user process.");
        return Mono.fromCallable(() -> realmResource.users().delete(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(response -> handleResponseStatus(response, HttpStatus.NO_CONTENT))
                .doFinally(signalType -> {
                    if (signalType == SignalType.ON_COMPLETE) {
                        log.info("User has been deleted from the keycloak auth-server.");
                    }
                });
    }

    @Override
    public Mono<Void> assignRole(String id, KeycloakRole keycloakRole) {
        log.info("Started user assigning role process.");
        UserResource userResource = realmResource.users().get(id);
        RolesResource rolesResource = realmResource.roles();

        log.debug("Getting equivalent keycloak role to argument role.");
        return Mono.fromCallable(() -> rolesResource.get(keycloakRole.getRole()))
                .subscribeOn(Schedulers.boundedElastic())
                .map(RoleResource::toRepresentation)
                .flatMap(roleRepresentation -> {
                    log.info("Assigning keycloak role to an user.");
                    return Mono.fromRunnable(() -> userResource.roles().realmLevel()
                            .add(Collections.singletonList(roleRepresentation))
                    ).subscribeOn(Schedulers.boundedElastic());
                }).then();
    }

    private static Mono<Void> handleResponseStatus(final Response response, final HttpStatus successStatus) {
        if (response.getStatus() != successStatus.value()) {
            log.error("Some error status occurred in keycloak user creation process response: {}", response.getStatusInfo());
            return Mono.error(new ErrorResponseException(HttpStatus.valueOf(response.getStatus())));
        }
        return Mono.empty();
    }
}