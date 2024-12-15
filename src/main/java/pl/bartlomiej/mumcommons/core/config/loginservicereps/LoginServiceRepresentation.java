package pl.bartlomiej.mumcommons.core.config.loginservicereps;

public record LoginServiceRepresentation(String hostname, int port, String loginResourceIdentifier, String clientId) {
}
