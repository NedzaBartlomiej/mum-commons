package pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.reactor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class DefaultErrorResponseModelServerAccessDeniedHandler implements ServerAccessDeniedHandler {

    private final ErrorResponseModelServerExceptionHandler errorResponseModelServerExceptionHandler;

    public DefaultErrorResponseModelServerAccessDeniedHandler(ErrorResponseModelServerExceptionHandler errorResponseModelServerExceptionHandler) {
        this.errorResponseModelServerExceptionHandler = errorResponseModelServerExceptionHandler;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return errorResponseModelServerExceptionHandler.processException(exchange, denied);
    }
}