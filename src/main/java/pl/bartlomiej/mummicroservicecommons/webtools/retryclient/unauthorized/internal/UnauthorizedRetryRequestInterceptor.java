package pl.bartlomiej.mummicroservicecommons.webtools.retryclient.unauthorized.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.ErrorResponseException;
import pl.bartlomiej.mummicroservicecommons.webtools.retryclient.RetryException;
import pl.bartlomiej.mummicroservicecommons.webtools.retryclient.unauthorized.external.RetryClientTokenProvider;

import java.io.IOException;

class UnauthorizedRetryRequestInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger log = LoggerFactory.getLogger(UnauthorizedRetryRequestInterceptor.class);

    private final RetryClientTokenProvider tokenProvider;

    UnauthorizedRetryRequestInterceptor(RetryClientTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    @NonNull
    public ClientHttpResponse intercept(@NonNull HttpRequest request, @NonNull byte[] body, @NonNull ClientHttpRequestExecution execution) throws IOException {
        log.info("Intercepting request using a http client with 401 retry system.");
        try {
            ClientHttpResponse response = execution.execute(request, body);
            if (response.getStatusCode().isSameCodeAs(HttpStatus.UNAUTHORIZED)) {
                log.info("Wrong access token in the request.");
                return this.retryUnauthorizedResponse(tokenProvider, request, body, execution);
            }
            log.info("Valid token, returning base response.");
            return response;
        } catch (Exception e) {
            log.error("Something went wrong during request interception. URI: {}", request.getURI(), e);
            throw new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private ClientHttpResponse retryUnauthorizedResponse(final RetryClientTokenProvider tokenProvider, final HttpRequest request, byte[] body, final ClientHttpRequestExecution execution) {
        log.info("Retrying an unauthorized request.");
        // todo - refresh token i the tokenProvider (todo - refreshToken() and static token storage)
        request.getHeaders().setBearerAuth(tokenProvider.getToken());
        try {
            ClientHttpResponse retriedResponse = execution.execute(request, body);
            log.info("Successful retry request, returning retried response.");
            return retriedResponse;
        } catch (Exception e) {
            throw new RetryException("Something went wrong when retrying the request.", e);
        }
    }
}