package pl.bartlomiej.globalidmservice.external.serviceidm.reactor;

import pl.bartlomiej.globalidmservice.external.keycloakidm.model.KeycloakUserRegistration;
import reactor.core.publisher.Mono;

/**
 * Reactive counterpart for IDMServiceTemplate
 */
public interface ReactiveIDMServiceTemplate<T> {
    Mono<T> create(KeycloakUserRegistration keycloakUserRegistration, String ipAddress);
}