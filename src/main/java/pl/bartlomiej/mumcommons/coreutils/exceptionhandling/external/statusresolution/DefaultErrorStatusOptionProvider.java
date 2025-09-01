package pl.bartlomiej.mumcommons.coreutils.exceptionhandling.external.statusresolution;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class DefaultErrorStatusOptionProvider implements ErrorStatusOptionProvider {

    private final Map<Class<? extends Throwable>, HttpStatus> statusOptions;

    public DefaultErrorStatusOptionProvider() {
        log.trace("Initializing status options map.");
        this.statusOptions = new HashMap<>();
        statusOptions.put(AccessDeniedException.class, HttpStatus.FORBIDDEN);
        statusOptions.put(AuthenticationException.class, HttpStatus.UNAUTHORIZED);
        statusOptions.put(AuthenticationCredentialsNotFoundException.class, HttpStatus.UNAUTHORIZED);
    }

    public Map<Class<? extends Throwable>, HttpStatus> getStatusOptions() {
        return Collections.unmodifiableMap(this.statusOptions);
    }
}
