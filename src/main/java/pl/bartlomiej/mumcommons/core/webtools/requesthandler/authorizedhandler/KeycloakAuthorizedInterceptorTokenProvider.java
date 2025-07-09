package pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler;

import org.keycloak.admin.client.token.TokenManager;

public class KeycloakAuthorizedInterceptorTokenProvider implements AuthorizedInterceptorTokenProvider {

    private final TokenManager keycloakTokenManager;

    public KeycloakAuthorizedInterceptorTokenProvider(TokenManager keycloakTokenManager) {
        this.keycloakTokenManager = keycloakTokenManager;
    }

    @Override
    public String getValidToken() {
        return keycloakTokenManager.getAccessTokenString();
    }
}
