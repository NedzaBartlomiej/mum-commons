package pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler.reactor;

import org.keycloak.admin.client.token.TokenManager;
import reactor.core.publisher.Mono;

public class KeycloakAuthorizedExchangeTokenProvider implements AuthorizedExchangeTokenProvider {

    private final TokenManager keycloakTokenManager;

    public KeycloakAuthorizedExchangeTokenProvider(TokenManager keycloakTokenManager) {
        this.keycloakTokenManager = keycloakTokenManager;
    }

    @Override
    public Mono<String> getValidToken() {
        return Mono.fromCallable(keycloakTokenManager::getAccessTokenString);
    }
}