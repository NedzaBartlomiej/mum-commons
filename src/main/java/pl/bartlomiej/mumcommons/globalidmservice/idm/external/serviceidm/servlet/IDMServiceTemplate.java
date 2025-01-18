package pl.bartlomiej.mumcommons.globalidmservice.idm.external.serviceidm.servlet;

import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.model.KeycloakUserRegistration;

/**
 * IDM service template for library clients to collect all IDM operations,
 * and remove unnecessary duplicate method declarations.
 *
 * @param <T> type of the user class
 */
public interface IDMServiceTemplate<T> {

    /**
     * @param keycloakUserRegistration interface for a T-user register DTO object
     * @param ipAddress                user's IP Address used to ip-auth-protection feature
     */
    T register(KeycloakUserRegistration keycloakUserRegistration, String ipAddress);

    T getEntity(String id);
}