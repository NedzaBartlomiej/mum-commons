package pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

// todo
public class MemoryAuthorizedExchangeTokenManager implements AuthorizedExchangeTokenManager {

    private static final Logger log = LoggerFactory.getLogger(MemoryAuthorizedExchangeTokenManager.class);
    private final AuthorizedExchangeTokenProvider tokenProvider;
    private final AtomicReference<String> tokenRef = new AtomicReference<>();
    private final AtomicReference<Mono<String>> fetchingMonoRef = new AtomicReference<>();
    private final AtomicReference<Mono<Void>> refreshingMonoRef = new AtomicReference<>();

    public MemoryAuthorizedExchangeTokenManager(AuthorizedExchangeTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Mono<String> getToken() {
        String currentToken = this.tokenRef.get();
        if (currentToken != null) {
            log.debug("Token isn't null, returning existing.");
            return Mono.just(currentToken);
        }

        Mono<String> newFetchingMono = Mono.defer(() -> this.tokenProvider.getValidToken()
                .doOnSuccess(fetchedToken -> {
                    log.debug("No token available, fetched new one.");
                    this.tokenRef.set(fetchedToken);
                })
                .doFinally(signalType -> this.fetchingMonoRef.set(null))
        );
        return this.fetchingMonoRef.updateAndGet(existingFetchingMono ->
                Objects.requireNonNullElseGet(existingFetchingMono, newFetchingMono::cache)
        );
    }

    // todo - fix it (described in test)
    @Override
    public Mono<Void> refreshToken() {
        Mono<Void> newRefreshingMono = Mono.defer(() -> this.tokenProvider.getValidToken()
                .doOnSuccess(ignored -> log.debug("Refreshing token. {}", Thread.currentThread().getName()))
                .doFinally(signalType -> this.refreshingMonoRef.set(null))
                .then()
        );
        return this.refreshingMonoRef.updateAndGet(existingRefreshingMono ->
                Objects.requireNonNullElseGet(existingRefreshingMono, newRefreshingMono::cache)
        );
    }
}