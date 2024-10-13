package pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.statusresolution;

import org.springframework.http.HttpStatus;

public interface GlobalHttpStatusResolver {
    HttpStatus resolveHttpStatus(Throwable exception);
}