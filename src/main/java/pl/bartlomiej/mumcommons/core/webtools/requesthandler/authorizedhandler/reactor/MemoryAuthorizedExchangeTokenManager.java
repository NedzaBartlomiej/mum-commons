package pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

// todo
public class MemoryAuthorizedExchangeTokenManager implements AuthorizedExchangeTokenManager {

    private static final Logger log = LoggerFactory.getLogger(MemoryAuthorizedExchangeTokenManager.class);
    private final AuthorizedExchangeTokenProvider tokenProvider;
    private String token; // czy pole w webflux do ktorego sie dostaje czyli to, musi byc zabezpieczone w kontekscie wielowatkowym?

    public MemoryAuthorizedExchangeTokenManager(AuthorizedExchangeTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Mono<String> getToken() {

    }

    @Override
    public Mono<Void> refreshToken() {
        log.info("Refreshing token.");

    }
}
