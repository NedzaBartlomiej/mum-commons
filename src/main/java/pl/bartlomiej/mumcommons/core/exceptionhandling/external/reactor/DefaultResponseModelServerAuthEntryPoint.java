package pl.bartlomiej.mumcommons.core.exceptionhandling.external.reactor;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class DefaultResponseModelServerAuthEntryPoint implements ServerAuthenticationEntryPoint {

    private final ResponseModelServerExceptionHandler responseModelServerExceptionHandler;

    public DefaultResponseModelServerAuthEntryPoint(ResponseModelServerExceptionHandler responseModelServerExceptionHandler) {
        this.responseModelServerExceptionHandler = responseModelServerExceptionHandler;
    }

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        return responseModelServerExceptionHandler.processException(exchange, ex);
    }
}
