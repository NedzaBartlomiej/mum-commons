package pl.bartlomiej.mumcommons.core.exceptionhandling.external.statusresolution;

import org.springframework.http.HttpStatus;

import java.util.Map;

public interface ErrorStatusOptionProvider {
    Map<Class<? extends Throwable>, HttpStatus> getStatusOptions();
}
