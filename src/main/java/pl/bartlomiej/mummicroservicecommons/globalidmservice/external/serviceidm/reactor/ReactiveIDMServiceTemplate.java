package pl.bartlomiej.mummicroservicecommons.globalidmservice.external.serviceidm.reactor;

import pl.bartlomiej.mummicroservicecommons.globalidmservice.external.keycloakidm.model.KeycloakUserRegistration;
import reactor.core.publisher.Mono;

/**
 * Reactive counterpart for IDMServiceTemplate
 */
public interface ReactiveIDMServiceTemplate<T> {
    Mono<T> create(KeycloakUserRegistration keycloakUserRegistration, String ipAddress);

    Mono<T> getEntity(String id);
}