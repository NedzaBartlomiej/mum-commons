package pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class AuthorizedExchangeFilterFunction implements ExchangeFilterFunction {

    private static final Logger log = LoggerFactory.getLogger(AuthorizedExchangeFilterFunction.class);
    private final AuthorizedExchangeTokenManager tokenManager;

    public AuthorizedExchangeFilterFunction(AuthorizedExchangeTokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    @NonNull
    public Mono<ClientResponse> filter(@NonNull ClientRequest request, @NonNull ExchangeFunction next) {
        log.info("Filtering request. URI: {}", request.url());
        return tokenManager.getToken()
                .flatMap(token -> this.setBearerAuth(request, token))
                .flatMap(next::exchange)
                .flatMap(response -> {
                    if (response.statusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)) {
                        log.info("Invalid exchange access token.");
                        return response.releaseBody()
                                .then(this.retryUnauthorizedResponse(request, next));
                    }
                    log.info("Valid exchange token, returning filtered response.");
                    return Mono.just(response);
                }).doOnError(throwable ->
                        log.error("Something went wrong during requesting. URI: {}", request.url(), throwable)
                );
    }

    private Mono<ClientResponse> retryUnauthorizedResponse(final ClientRequest request, final ExchangeFunction next) {
        log.info("Retrying an unauthorized request. URI: {}", request.url());
        return this.tokenManager.refreshToken()
                .flatMap(newToken -> this.setBearerAuth(request, newToken))
                .flatMap(next::exchange)
                .doOnSuccess(retriedResponse ->
                        log.info("Successful retry request, returning retried response.")
                )
                .doOnError(throwable ->
                        log.error("Something went wrong when retrying the request.", throwable)
                );
    }

    private Mono<ClientRequest> setBearerAuth(final ClientRequest request, final String token) {
        log.info("Setting a bearer auth header in request.");
        return Mono.just(token)
                .map(t -> ClientRequest.from(request)
                        .headers(headers -> headers.setBearerAuth(t))
                        .build())
                .doOnSuccess(r -> log.info("Bearer token set successfully."))
                .doOnError(e -> log.error("Failed to set bearer token.", e));
    }
}