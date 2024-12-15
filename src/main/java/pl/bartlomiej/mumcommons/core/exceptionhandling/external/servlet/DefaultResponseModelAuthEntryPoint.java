package pl.bartlomiej.mumcommons.core.exceptionhandling.external.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class DefaultResponseModelAuthEntryPoint implements AuthenticationEntryPoint {

    private final ResponseModelExceptionHandler exceptionHandler;

    public DefaultResponseModelAuthEntryPoint(ResponseModelExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        exceptionHandler.processException(response, authException);
    }
}
