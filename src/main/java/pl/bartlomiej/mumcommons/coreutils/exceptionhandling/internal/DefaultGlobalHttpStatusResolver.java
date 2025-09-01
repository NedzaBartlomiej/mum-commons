package pl.bartlomiej.mumcommons.coreutils.exceptionhandling.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import pl.bartlomiej.mumcommons.coreutils.exceptionhandling.external.statusresolution.ErrorStatusOptionProvider;
import pl.bartlomiej.mumcommons.coreutils.exceptionhandling.external.statusresolution.GlobalHttpStatusResolver;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

class DefaultGlobalHttpStatusResolver implements GlobalHttpStatusResolver {
    private static final Logger log = LoggerFactory.getLogger(DefaultGlobalHttpStatusResolver.class);
    private final Map<Class<? extends Throwable>, HttpStatus> statusOptions;

    DefaultGlobalHttpStatusResolver(ErrorStatusOptionProvider errorStatusOptionProvider) {
        this.statusOptions = errorStatusOptionProvider.getStatusOptions();
    }

    @Override
    public HttpStatus resolveHttpStatus(Throwable exception) {
        log.debug("Resolving http status for the error response.");


        log.debug("Resolving by base class.");
        HttpStatus status = statusOptions.get(exception.getClass());

        log.debug("Failed resolution by base class, resolving by superclasses.");
        if (status == null) {
            AtomicReference<HttpStatus> httpStatusAtomicReference = new AtomicReference<>(HttpStatus.INTERNAL_SERVER_ERROR);
            statusOptions.forEach((clazz, httpStatus) -> {
                if (clazz.isAssignableFrom(exception.getClass())) {
                    httpStatusAtomicReference.set(httpStatus);
                }
            });
            status = httpStatusAtomicReference.get();
        }

        return status;
    }
}