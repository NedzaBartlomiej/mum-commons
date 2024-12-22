package pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler.reactor;

import reactor.core.publisher.Mono;

public interface AuthorizedExchangeTokenProvider {
    Mono<String> getValidToken();
}
