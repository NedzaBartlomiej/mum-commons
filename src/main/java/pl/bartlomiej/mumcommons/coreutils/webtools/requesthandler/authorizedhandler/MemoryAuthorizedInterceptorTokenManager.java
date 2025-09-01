package pl.bartlomiej.mumcommons.coreutils.webtools.requesthandler.authorizedhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemoryAuthorizedInterceptorTokenManager implements AuthorizedInterceptorTokenManager {

    private static final Logger log = LoggerFactory.getLogger(MemoryAuthorizedInterceptorTokenManager.class);
    private final AuthorizedInterceptorTokenProvider tokenProvider;
    private String token;

    public MemoryAuthorizedInterceptorTokenManager(AuthorizedInterceptorTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public String getToken() {
        if (token == null) {
            log.trace("Token is null.");
            this.refreshToken();
        }
        return token;
    }

    @Override
    public void refreshToken() {
        log.trace("Refreshing token.");
        token = tokenProvider.getValidToken();
    }
}