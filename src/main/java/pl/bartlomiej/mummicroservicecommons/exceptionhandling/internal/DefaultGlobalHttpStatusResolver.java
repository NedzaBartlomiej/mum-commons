package pl.bartlomiej.mummicroservicecommons.exceptionhandling.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.GlobalHttpStatusResolver;

import java.util.HashMap;
import java.util.Map;

class DefaultGlobalHttpStatusResolver implements GlobalHttpStatusResolver {
    private static final Logger log = LoggerFactory.getLogger(DefaultGlobalHttpStatusResolver.class);
    private static final Map<Class<? extends Throwable>, HttpStatus> resolverOptions = new HashMap<>();

    static {
        log.debug("Initialization of basic resolver options.");
        resolverOptions.put(AccessDeniedException.class, HttpStatus.FORBIDDEN);
        resolverOptions.put(AuthenticationException.class, HttpStatus.UNAUTHORIZED);
        resolverOptions.put(AuthenticationCredentialsNotFoundException.class, HttpStatus.UNAUTHORIZED);
        resolverOptions.put(WebExchangeBindException.class, HttpStatus.BAD_REQUEST);
        resolverOptions.put(MethodNotAllowedException.class, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    public HttpStatus resolveHttpStatus(Throwable exception) {
        log.debug("Resolving http status for the error response.");

        log.debug("Resolving by base class.");
        HttpStatus status = resolverOptions.get(exception.getClass());

        log.debug("Failed resolution by base class, resolving by superclasses.");
        if (status == null) {
            final HttpStatus[] resolvedStatus = {HttpStatus.INTERNAL_SERVER_ERROR};

            resolverOptions.forEach((clazz, httpStatus) -> {
                if (clazz.isAssignableFrom(exception.getClass())) {
                    resolvedStatus[0] = httpStatus;
                }
            });

            return resolvedStatus[0];
        }

        return status;
    }
}