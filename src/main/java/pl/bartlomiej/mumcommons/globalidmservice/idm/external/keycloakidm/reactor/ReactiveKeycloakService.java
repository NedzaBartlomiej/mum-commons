package pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.reactor;

import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model.KeycloakRole;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model.KeycloakUserRegistration;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model.KeycloakUserRepresentation;
import reactor.core.publisher.Mono;

public interface ReactiveKeycloakService {
    Mono<KeycloakUserRepresentation> create(KeycloakUserRegistration keycloakUserRegistration);

    Mono<Void> delete(String id);

    Mono<Void> assignClientRole(String id, KeycloakRole keycloakRole);

    Mono<String> getAccessToken();
}