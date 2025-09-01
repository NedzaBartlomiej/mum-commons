package pl.bartlomiej.mumcommons.coreutils.exceptionhandling.external;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class DefaultResponseModelAccessDeniedHandler implements AccessDeniedHandler {

    private final ResponseModelExceptionHandler exceptionHandler;

    public DefaultResponseModelAccessDeniedHandler(ResponseModelExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        exceptionHandler.processException(response, accessDeniedException);
    }
}
