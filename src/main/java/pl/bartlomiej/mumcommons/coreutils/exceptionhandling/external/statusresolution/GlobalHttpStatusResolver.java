package pl.bartlomiej.mumcommons.coreutils.exceptionhandling.external.statusresolution;

import org.springframework.http.HttpStatus;

public interface GlobalHttpStatusResolver {
    HttpStatus resolveHttpStatus(Throwable exception);
}