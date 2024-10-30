package pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.reactor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class DefaultResponseModelServerAccessDeniedHandler implements ServerAccessDeniedHandler {

    private final ResponseModelServerExceptionHandler responseModelServerExceptionHandler;

    public DefaultResponseModelServerAccessDeniedHandler(ResponseModelServerExceptionHandler responseModelServerExceptionHandler) {
        this.responseModelServerExceptionHandler = responseModelServerExceptionHandler;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return responseModelServerExceptionHandler.processException(exchange, denied);
    }
}