package pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class DefaultErrorResponseModelAuthEntryPoint implements AuthenticationEntryPoint {

    private final ErrorResponseModelExceptionHandler exceptionHandler;

    public DefaultErrorResponseModelAuthEntryPoint(ErrorResponseModelExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        exceptionHandler.processException(response, authException);
    }
}
