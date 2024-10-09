package pl.bartlomiej.mummicroservicecommons.globalidmservice.external.serviceidm.servlet;

import pl.bartlomiej.mummicroservicecommons.globalidmservice.external.keycloakidm.model.KeycloakUserRegistration;

/**
 * IDM service template for library clients to collect all IDM operations,
 * and remove unnecessary duplicate method declarations.
 *
 * @param <T> type of the user class
 */
public interface IDMServiceTemplate<T> {

    /**
     * @param keycloakUserRegistration interface for T-user register DTO object
     * @param ipAddress                user's IP Address used to ip-auth-protection feature
     */
    T create(KeycloakUserRegistration keycloakUserRegistration, String ipAddress);
}