package pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class MemoryAuthorizedExchangeTokenManager implements AuthorizedExchangeTokenManager {

    private static final Logger log = LoggerFactory.getLogger(MemoryAuthorizedExchangeTokenManager.class);
    private final AuthorizedExchangeTokenProvider tokenProvider;
    private final AtomicReference<Mono<String>> tokenRef = new AtomicReference<>();
    private final AtomicReference<Mono<Void>> refreshingMonoRef = new AtomicReference<>();

    public MemoryAuthorizedExchangeTokenManager(AuthorizedExchangeTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Mono<String> getToken() {
        Mono<String> token = this.tokenRef.get();
        if (token != null) return token;
        return this.tokenRef.updateAndGet(currentToken ->
                currentToken != null
                        ? currentToken
                        : this.tokenProvider.getValidToken().cache()
        );
    }

    @Override
    public Mono<Void> refreshToken() {
        Mono<Void> newRefreshingMono = Mono.defer(() -> this.tokenProvider.getValidToken()
                .doOnSuccess(fetchedToken -> {
                    log.debug("Refreshing token in thread: {}", Thread.currentThread().getName());
                    this.tokenRef.set(Mono.just(fetchedToken));
                })
                .doFinally(signalType -> this.refreshingMonoRef.set(null))
                .then()
        );
        return this.refreshingMonoRef.updateAndGet(existingRefreshingMono ->
                Objects.requireNonNullElseGet(existingRefreshingMono, newRefreshingMono::cache)
        );
    }
}