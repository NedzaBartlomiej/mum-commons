package pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class DefaultErrorResponseModelAccessDeniedHandler implements AccessDeniedHandler {

    private final ErrorResponseModelExceptionHandler exceptionHandler;

    public DefaultErrorResponseModelAccessDeniedHandler(ErrorResponseModelExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        exceptionHandler.processException(response, accessDeniedException);
    }
}
