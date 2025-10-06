package pl.bartlomiej.mumcommons.coreutils.webtools.requesthandler.authorizedhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.ErrorResponseException;
import pl.bartlomiej.mumcommons.coreutils.constants.LogTraceConstants;
import pl.bartlomiej.mumcommons.coreutils.webtools.requesthandler.RetryException;

import java.io.IOException;
import java.util.UUID;

public class AuthorizedRequestInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger log = LoggerFactory.getLogger(AuthorizedRequestInterceptor.class);
    private final AuthorizedInterceptorTokenManager authorizedInterceptorTokenManager;

    public AuthorizedRequestInterceptor(AuthorizedInterceptorTokenManager authorizedInterceptorTokenManager) {
        this.authorizedInterceptorTokenManager = authorizedInterceptorTokenManager;
    }

    @Override
    @NonNull
    public ClientHttpResponse intercept(@NonNull HttpRequest request, @NonNull byte[] body, @NonNull ClientHttpRequestExecution execution) throws IOException {
        try {
            if (MDC.get(LogTraceConstants.TRACE_ID) == null) {
                MDC.put(LogTraceConstants.TRACE_ID, UUID.randomUUID().toString());
            }

            log.debug("Intercepting request. URI: {}", request.getURI());

            log.trace("Setting a bearer auth header in request.");
            request.getHeaders().setBearerAuth(authorizedInterceptorTokenManager.getToken());

            log.trace("Executing request.");
            ClientHttpResponse response = execution.execute(request, body);

            if (response.getStatusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)) {
                log.trace("Invalid access token in the request.");
                return this.retryUnauthorizedResponse(request, body, execution);
            }

            log.trace("Valid token, returning base response.");
            return response;
        } catch (Exception e) {
            log.error("Something went wrong during request interception. URI: {}", request.getURI(), e);
            throw new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        } finally {
            MDC.clear();
        }
    }

    private ClientHttpResponse retryUnauthorizedResponse(final HttpRequest request, byte[] body, final ClientHttpRequestExecution execution) {
        try {
            log.debug("Retrying an unauthorized request. URI: {}", request.getURI());
            authorizedInterceptorTokenManager.refreshToken();
            request.getHeaders().setBearerAuth(authorizedInterceptorTokenManager.getToken());
            ClientHttpResponse retriedResponse = execution.execute(request, body);
            log.trace("Successful retry request, returning retried response.");
            return retriedResponse;
        } catch (Exception e) {
            throw new RetryException("Something went wrong when retrying the request.", e);
        }
    }
}