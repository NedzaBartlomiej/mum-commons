package pl.bartlomiej.mumcommons.globalidmservice.idm.internal.serviceidm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import pl.bartlomiej.mumcommons.core.offsettransaction.OffsetTransactionOperator;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model.KeycloakUserRegistration;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model.KeycloakUserRepresentation;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.KeycloakService;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.serviceidm.IDMServiceTemplate;

/**
 * Abstract IDM service for library clients to collect all IDM methods,
 * and remove code duplications for common IDM operations.
 *
 * @param <T> type of the user class
 */
public abstract class AbstractIDMService<T> implements IDMServiceTemplate<T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractIDMService.class);
    private final KeycloakService keycloakService;
    private final CrudRepository<T, String> crudRepository;

    protected AbstractIDMService(KeycloakService keycloakService, CrudRepository<T, String> crudRepository) {
        this.keycloakService = keycloakService;
        this.crudRepository = crudRepository;
    }

    protected abstract T createEntity(KeycloakUserRepresentation keycloakUserRepresentation, String ipAddress);

    protected abstract String getEntityId(T entity);

    @Override
    public T register(final KeycloakUserRegistration keycloakUserRegistration, final String ipAddress) {
        log.info("Started user creation process.");
        var keycloakUserRepresentation = keycloakService.create(keycloakUserRegistration);
        T entity = this.createEntity(keycloakUserRepresentation, ipAddress);

        log.info("Saving created keycloak user in the database.");
        return OffsetTransactionOperator.performOffsetFunctionTransaction(
                entity,
                this.getEntityId(entity),
                crudRepository::save,
                keycloakService::delete
        );
    }

    @Override
    public T getEntity(final String id) {
        return this.crudRepository.findById(id).orElseThrow(() -> new ErrorResponseException(HttpStatus.NOT_FOUND));
    }
}