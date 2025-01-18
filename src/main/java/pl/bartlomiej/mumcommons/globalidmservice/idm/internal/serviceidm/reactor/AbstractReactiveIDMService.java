package pl.bartlomiej.mumcommons.globalidmservice.idm.internal.serviceidm.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import pl.bartlomiej.mumcommons.core.offsettransaction.reactor.ReactiveOffsetTransactionOperator;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model.KeycloakUserRegistration;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model.KeycloakUserRepresentation;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.reactor.ReactiveKeycloakService;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.serviceidm.reactor.ReactiveIDMServiceTemplate;
import reactor.core.publisher.Mono;

/**
 * Reactive counterpart for AbstractIDMService
 */
public abstract class AbstractReactiveIDMService<T> implements ReactiveIDMServiceTemplate<T> {

    private static final Logger log = LoggerFactory.getLogger(AbstractReactiveIDMService.class);
    private final ReactiveKeycloakService reactiveKeycloakService;
    private final ReactiveCrudRepository<T, String> reactiveCrudRepository;

    protected AbstractReactiveIDMService(ReactiveKeycloakService reactiveKeycloakService, ReactiveCrudRepository<T, String> reactiveCrudRepository) {
        this.reactiveKeycloakService = reactiveKeycloakService;
        this.reactiveCrudRepository = reactiveCrudRepository;
    }

    protected abstract T createEntity(KeycloakUserRepresentation keycloakUserRepresentation, String ipAddress);

    protected abstract String getEntityId(T entity);

    @Override
    public Mono<T> register(final KeycloakUserRegistration keycloakUserRegistration, final String ipAddress) {
        log.info("Started user creation process.");
        return reactiveKeycloakService.create(keycloakUserRegistration)
                .flatMap(keycloakUserRepresentation -> {
                    T entity = this.createEntity(keycloakUserRepresentation, ipAddress);

                    log.info("Saving created keycloak user in the database.");
                    return ReactiveOffsetTransactionOperator.performOffsetFunctionTransaction(
                            entity,
                            this.getEntityId(entity),
                            reactiveCrudRepository::save,
                            reactiveKeycloakService::delete
                    );
                });
    }

    @Override
    public Mono<T> getEntity(String id) {
        return reactiveCrudRepository.findById(id)
                .switchIfEmpty(Mono.error(new ErrorResponseException(HttpStatus.NOT_FOUND)));
    }
}
