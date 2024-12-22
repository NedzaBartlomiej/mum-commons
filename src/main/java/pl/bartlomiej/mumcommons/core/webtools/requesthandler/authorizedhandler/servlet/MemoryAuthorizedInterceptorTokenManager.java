package pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemoryAuthorizedInterceptorTokenManager implements AuthorizedInterceptorTokenManager {

    private static final Logger log = LoggerFactory.getLogger(MemoryAuthorizedInterceptorTokenManager.class);
    private final AuthorizedInterceptorTokenProvider tokenProvider;
    private String token;

    public MemoryAuthorizedInterceptorTokenManager(AuthorizedInterceptorTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public String getToken() {
        if (token == null) {
            log.debug("Token is null.");
            this.refreshToken();
        }
        return token;
    }

    public void refreshToken() {
        log.info("Refreshing token.");
        token = tokenProvider.getValidToken();
    }
}