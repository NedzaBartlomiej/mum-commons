package pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.reactor;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class DefaultErrorResponseModelServerAuthEntryPoint implements ServerAuthenticationEntryPoint {

    private final ErrorResponseModelServerExceptionHandler errorResponseModelServerExceptionHandler;

    public DefaultErrorResponseModelServerAuthEntryPoint(ErrorResponseModelServerExceptionHandler errorResponseModelServerExceptionHandler) {
        this.errorResponseModelServerExceptionHandler = errorResponseModelServerExceptionHandler;
    }

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        return errorResponseModelServerExceptionHandler.processException(exchange, ex);
    }
}
