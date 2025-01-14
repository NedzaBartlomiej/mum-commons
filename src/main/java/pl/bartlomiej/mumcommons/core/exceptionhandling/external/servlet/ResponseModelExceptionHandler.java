package pl.bartlomiej.mumcommons.core.exceptionhandling.external.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponseException;
import pl.bartlomiej.mumcommons.core.exceptionhandling.external.statusresolution.GlobalHttpStatusResolver;
import pl.bartlomiej.mumcommons.core.model.response.ResponseModel;

import java.io.IOException;

public class ResponseModelExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ResponseModelExceptionHandler.class);
    private final ObjectMapper objectMapper;
    private final GlobalHttpStatusResolver globalHttpStatusResolver;

    public ResponseModelExceptionHandler(ObjectMapper objectMapper, GlobalHttpStatusResolver globalHttpStatusResolver) {
        this.objectMapper = objectMapper;
        this.globalHttpStatusResolver = globalHttpStatusResolver;
    }

    public void processException(HttpServletResponse response, final Throwable exception) {
        log.debug("Processing an exception.");
        log.error("Exception Message: {}, Exception: {}", exception.getMessage(), exception.getClass());
        HttpStatus httpStatus = globalHttpStatusResolver.resolveHttpStatus(exception);
        var responseModel = ResponseModel.buildBasicErrorResponseModel(httpStatus, httpStatus.getReasonPhrase());
        try {
            this.writeResponse(response, responseModel);
        } catch (IOException e) {
            log.error("Something went wrong while writing the response.", e);
            throw new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private void writeResponse(HttpServletResponse response, final ResponseModel<Void> responseModel) throws IOException {
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
