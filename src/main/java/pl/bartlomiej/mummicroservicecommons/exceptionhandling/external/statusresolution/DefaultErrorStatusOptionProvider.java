package pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.statusresolution;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.Map;

public class DefaultErrorStatusOptionProvider implements ErrorStatusOptionProvider {

    private final Map<Class<? extends Throwable>, HttpStatus> statusOptions;

    public DefaultErrorStatusOptionProvider() {
        this.statusOptions = new HashMap<>();
        statusOptions.put(AccessDeniedException.class, HttpStatus.FORBIDDEN);
        statusOptions.put(AuthenticationException.class, HttpStatus.UNAUTHORIZED);
        statusOptions.put(AuthenticationCredentialsNotFoundException.class, HttpStatus.UNAUTHORIZED);
    }

    public final Map<Class<? extends Throwable>, HttpStatus> getStatusOptions() {
        return new HashMap<>(statusOptions);
    }
}
