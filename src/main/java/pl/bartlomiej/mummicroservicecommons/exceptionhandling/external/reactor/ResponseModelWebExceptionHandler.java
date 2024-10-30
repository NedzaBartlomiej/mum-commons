package pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.reactor;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import pl.bartlomiej.mummicroservicecommons.model.response.ResponseModel;
import reactor.core.publisher.Mono;

import java.util.Map;

@Order(-2)
public class ResponseModelWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public ResponseModelWebExceptionHandler(ErrorAttributes errorAttributes,
                                            WebProperties webProperties,
                                            ApplicationContext applicationContext,
                                            ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest serverRequest) {

        final ErrorAttributeOptions errorAttributeOptions = ErrorAttributeOptions.of(ErrorAttributeOptions.Include.values());
        final Map<String, Object> errorAttributes = super.getErrorAttributes(serverRequest, errorAttributeOptions);

        final int statusCode = Integer.parseInt(errorAttributes.get("status").toString());
        final HttpStatus httpStatus = HttpStatus.valueOf(statusCode);

        ResponseModel<Void> responseModel = ResponseModel.buildBasicErrorResponseModel(httpStatus,
                getErrorMessage(httpStatus, errorAttributes)
        );

        return ServerResponse
                .status(statusCode)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(responseModel));
    }

    private static String getErrorMessage(HttpStatus httpStatus, Map<String, Object> errorAttributes) {
        String message;
        if (httpStatus.is5xxServerError()) {
            message = "Something went wrong on the server side, please try again or report the problem.";
        } else {
            message = errorAttributes.get("message").toString();
        }
        return message;
    }
}