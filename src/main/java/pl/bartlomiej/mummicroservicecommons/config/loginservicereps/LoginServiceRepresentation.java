package pl.bartlomiej.mummicroservicecommons.config.loginservicereps;

public record LoginServiceRepresentation(String hostname, int port, String loginResourceIdentifier, String clientId) {
}
