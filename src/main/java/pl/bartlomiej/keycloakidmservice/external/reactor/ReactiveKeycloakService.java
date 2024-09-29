package pl.bartlomiej.keycloakidmservice.external.reactor;

import pl.bartlomiej.keycloakidmservice.external.model.KeycloakRole;
import pl.bartlomiej.keycloakidmservice.external.model.KeycloakUserRegistration;
import pl.bartlomiej.keycloakidmservice.external.model.KeycloakUserRepresentation;
import reactor.core.publisher.Mono;

public interface ReactiveKeycloakService {
    Mono<KeycloakUserRepresentation> create(KeycloakUserRegistration keycloakUserRegistration);

    Mono<Void> delete(String id);

    Mono<Void> assignRole(String id, KeycloakRole keycloakRole);
}
