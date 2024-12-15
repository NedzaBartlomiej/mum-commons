package pl.bartlomiej.mumcommons.core.config.loginservicereps;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "login-service-reps")
public record LoginServiceRepsProperties(List<LoginServiceRepresentation> loginServiceRepresentations) {

}