package pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.reactor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.server.ServerWebExchange;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.ErrorResponseModel;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.GlobalHttpStatusResolver;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class ErrorResponseModelServerExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ErrorResponseModelServerExceptionHandler.class);
    private final GlobalHttpStatusResolver globalHttpStatusResolver;
    private final ObjectMapper objectMapper;

    public ErrorResponseModelServerExceptionHandler(ObjectMapper objectMapper, GlobalHttpStatusResolver globalHttpStatusResolver) {
        this.objectMapper = objectMapper;
        this.globalHttpStatusResolver = globalHttpStatusResolver;
    }

    public Mono<Void> processException(ServerWebExchange exchange, final Throwable exception) {
        log.debug("Processing an exception.");
        log.error("Exception Message: {}, Exception: {}", exception.getMessage(), exception.getClass());
        final HttpStatus httpStatus = globalHttpStatusResolver.resolveHttpStatus(exception);
        final ErrorResponseModel responseModel = new ErrorResponseModel(
                httpStatus, httpStatus.value(), httpStatus.getReasonPhrase()
        );
        return this.writeResponse(exchange.getResponse(), responseModel)
                .onErrorResume(e -> {
                    log.error("Something went wrong while writing the response.", e);
                    return Mono.error(new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, e));
                });
    }

    private Mono<Void> writeResponse(ServerHttpResponse response, final ErrorResponseModel responseModel) {
        log.debug("Started the process of writing an error response.");

        log.debug("Setting {} to {}", HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        log.debug("Setting error response status.");
        response.setStatusCode(responseModel.getHttpStatus());

        log.debug("Mapping error response model.");
        Mono<DataBuffer> responseBodyBufferMono = Mono.fromCallable(() -> objectMapper.writeValueAsString(responseModel))
                .map(responseBody -> response
                        .bufferFactory()
                        .wrap(responseBody.getBytes(StandardCharsets.UTF_8))
                );

        log.debug("Writing error response body.");
        return response.writeWith(responseBodyBufferMono);
    }
}