package pl.bartlomiej.mummicroservicecommons.globalidmservice.external.keycloakidm.reactor;

import pl.bartlomiej.mummicroservicecommons.globalidmservice.external.keycloakidm.model.KeycloakRole;
import pl.bartlomiej.mummicroservicecommons.globalidmservice.external.keycloakidm.model.KeycloakUserRegistration;
import pl.bartlomiej.mummicroservicecommons.globalidmservice.external.keycloakidm.model.KeycloakUserRepresentation;
import reactor.core.publisher.Mono;

public interface ReactiveKeycloakService {
    Mono<KeycloakUserRepresentation> create(KeycloakUserRegistration keycloakUserRegistration);

    Mono<Void> delete(String id);

    Mono<Void> assignClientRole(String id, KeycloakRole keycloakRole);

    Mono<String> getAccessToken();
}