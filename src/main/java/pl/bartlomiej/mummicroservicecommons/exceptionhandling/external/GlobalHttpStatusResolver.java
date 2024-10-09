package pl.bartlomiej.mummicroservicecommons.exceptionhandling.external;

import org.springframework.http.HttpStatus;

public interface GlobalHttpStatusResolver {
    HttpStatus resolveHttpStatus(Throwable exception);
}