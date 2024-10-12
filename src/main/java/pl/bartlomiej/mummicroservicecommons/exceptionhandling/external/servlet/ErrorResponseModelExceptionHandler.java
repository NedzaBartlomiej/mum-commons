package pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponseException;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.ErrorResponseModel;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.GlobalHttpStatusResolver;

import java.io.IOException;

public class ErrorResponseModelExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ErrorResponseModelExceptionHandler.class);
    private final ObjectMapper objectMapper;
    private final GlobalHttpStatusResolver globalHttpStatusResolver;

    public ErrorResponseModelExceptionHandler(ObjectMapper objectMapper, GlobalHttpStatusResolver globalHttpStatusResolver) {
        this.objectMapper = objectMapper;
        this.globalHttpStatusResolver = globalHttpStatusResolver;
    }

    public void processException(HttpServletResponse response, final Throwable exception) {
        log.debug("Processing an exception.");
        log.error("Exception Message: {}, Exception: {}", exception.getMessage(), exception.getClass());
        final HttpStatus httpStatus = globalHttpStatusResolver.resolveHttpStatus(exception);
        final ErrorResponseModel responseModel = new ErrorResponseModel(httpStatus, httpStatus.value(), httpStatus.getReasonPhrase());
        try {
            this.writeResponse(response, responseModel);
        } catch (IOException e) {
            log.error("Something went wrong while writing the response.", e);
            throw new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private void writeResponse(HttpServletResponse response, final ErrorResponseModel responseModel) throws IOException {
        log.debug("Started the process of writing an error response.");

        log.debug("Setting {} to {}", HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        log.debug("Setting error response status.");
        response.setStatus(responseModel.getHttpStatusCode());

        log.debug("Mapping error response model.");
        String responseBody = objectMapper.writeValueAsString(responseModel);

        log.debug("Writing error response body.");
        response.getWriter().write(responseBody);
    }
}
