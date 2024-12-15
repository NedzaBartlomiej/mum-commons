package pl.bartlomiej.mumcommons.core.webtools.retry.unauthorized;

import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.servlet.KeycloakService;

// todo - RetryClientTokenProvider abstract class wrapper which will provide the "token storage" with:
//  - getValidToken() { private refreshToken() } and private abstract getToken() - (new token from an inheritor like this)
public class KeycloakRetryClientTokenProvider implements RetryClientTokenProvider {

    private final KeycloakService keycloakService;

    public KeycloakRetryClientTokenProvider(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    public String getValidToken() {
        return keycloakService.getServiceAccessToken();
    }
}