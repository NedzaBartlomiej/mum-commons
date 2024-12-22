package pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler.reactor;

import reactor.core.publisher.Mono;

public interface AuthorizedExchangeTokenManager {
    Mono<String> getToken();

    Mono<Void> refreshToken();
}
