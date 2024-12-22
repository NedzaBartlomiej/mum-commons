package pl.bartlomiej.mumcommons.globalidmservice.idm.internal.keycloakidm;

import org.keycloak.admin.client.Keycloak;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model.KeycloakUserRegistration;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model.KeycloakUserRepresentation;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.reactor.ReactiveKeycloakService;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.servlet.KeycloakService;
import reactor.core.publisher.Mono;

public class DefaultReactiveKeycloakService extends AbstractKeycloakService implements ReactiveKeycloakService {
    private final KeycloakService keycloakService;

    DefaultReactiveKeycloakService(KeycloakProperties properties, Keycloak keycloak, KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
        keycloak.realm(properties.realmName());
    }

    @Override
    public Mono<KeycloakUserRepresentation> create(KeycloakUserRegistration keycloakUserRegistration) {
        return Mono.fromCallable(() -> keycloakService.create(keycloakUserRegistration));
    }

    @Override
    public Mono<Void> delete(String id) {
        return Mono.fromRunnable(() -> keycloakService.delete(id));
    }

}