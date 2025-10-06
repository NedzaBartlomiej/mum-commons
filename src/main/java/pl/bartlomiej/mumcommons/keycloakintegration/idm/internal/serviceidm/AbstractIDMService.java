package pl.bartlomiej.mumcommons.keycloakintegration.idm.internal.serviceidm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import pl.bartlomiej.mumcommons.coreutils.constants.LogTraceConstants;
import pl.bartlomiej.mumcommons.coreutils.offsettransaction.OffsetTransactionOperator;
import pl.bartlomiej.mumcommons.keycloakintegration.idm.external.keycloakidm.KeycloakService;
import pl.bartlomiej.mumcommons.keycloakintegration.idm.external.keycloakidm.model.KeycloakUserRegistration;
import pl.bartlomiej.mumcommons.keycloakintegration.idm.external.keycloakidm.model.KeycloakUserRepresentation;
import pl.bartlomiej.mumcommons.keycloakintegration.idm.external.serviceidm.IDMServiceTemplate;

import java.util.UUID;

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

    // TODO: to refine MDC context - generally it works correctly (meaning about pattern etc. etc.)
    //  I need to learn a little bit more about this, cause I don't have context everywhere:
    //  Transaction & Deletion don't have traceId - they should have the same traceId as Creation!
    // I just need to figure out the MDC context a little bit more
    // to make it fully understandable and operate it without any problems
    @Override
    public T register(final KeycloakUserRegistration keycloakUserRegistration, final String ipAddress) {
        try {
            if (MDC.get(LogTraceConstants.TRACE_ID) == null) {
                MDC.put(LogTraceConstants.TRACE_ID, UUID.randomUUID().toString());
            }

            log.info("Started user creation process.");
            if (ipAddress == null || ipAddress.isBlank()) {
                throw new IllegalArgumentException("Required registered user IP address is null or blank.");
            }

            var keycloakUserRepresentation = keycloakService.create(keycloakUserRegistration);
            T entity = this.createEntity(keycloakUserRepresentation, ipAddress);

            log.info("Saving created keycloak user in the database."); // ^^TODO^^ THIS LINE ALSO DOESN'T HAVE TRACEID RELATED TO THE CREATION FLOW
            return OffsetTransactionOperator.performOffsetFunctionTransaction(
                    entity,
                    this.getEntityId(entity),
                    crudRepository::save,
                    keycloakService::delete
            );
        } finally {
            MDC.clear();
        }
    }

    @Override
    public T getEntity(final String id) {
        return this.crudRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Invalid id, Entity not found.");
                    return new ErrorResponseException(HttpStatus.NOT_FOUND);
                });
    }
}