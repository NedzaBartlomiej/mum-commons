package pl.bartlomiej.mumcommons.core.webtools.requestinterceptor.authorizedinterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.ErrorResponseException;
import pl.bartlomiej.mumcommons.core.webtools.requestinterceptor.RetryException;

import java.io.IOException;

public class AuthorizedRequestInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger log = LoggerFactory.getLogger(AuthorizedRequestInterceptor.class);

    private final AuthorizedInterceptorTokenManager authorizedInterceptorTokenManager;

    public AuthorizedRequestInterceptor(AuthorizedInterceptorTokenManager authorizedInterceptorTokenManager) {
        this.authorizedInterceptorTokenManager = authorizedInterceptorTokenManager;
    }

    @Override
    @NonNull
    public ClientHttpResponse intercept(@NonNull HttpRequest request, @NonNull byte[] body, @NonNull ClientHttpRequestExecution execution) throws IOException {
        log.info("Intercepting request. URI: {}", request.getURI());
        try {
            log.info("Setting a bearer auth header in request.");
            request.getHeaders().setBearerAuth(authorizedInterceptorTokenManager.getToken());

            log.info("Executing request.");
            ClientHttpResponse response = execution.execute(request, body);

            if (response.getStatusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)) {
                log.info("Invalid access token in the request.");
                return this.retryUnauthorizedResponse(request, body, execution);
            }

            log.info("Valid token, returning base response.");
            return response;
        } catch (Exception e) {
            log.error("Something went wrong during request interception. URI: {}", request.getURI(), e);
            throw new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private ClientHttpResponse retryUnauthorizedResponse(final HttpRequest request, byte[] body, final ClientHttpRequestExecution execution) {
        log.info("Retrying an unauthorized request.");
        try {
            authorizedInterceptorTokenManager.refreshToken();
            request.getHeaders().setBearerAuth(authorizedInterceptorTokenManager.getToken());
            ClientHttpResponse retriedResponse = execution.execute(request, body);
            log.info("Successful retry request, returning retried response.");
            return retriedResponse;
        } catch (Exception e) {
            throw new RetryException("Something went wrong when retrying the request.", e);
        }
    }
}