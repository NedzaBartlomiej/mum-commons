package pl.bartlomiej.mummicroservicecommons.exceptionhandling.external;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Objects;

@RestControllerAdvice
public class ErrorResponseModelGlobalRestControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseModel> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<ErrorResponseModel> handleNoContentException(NoContentException e) {
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseModel> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponseModel> handleValidationException(BindingResult bindingResult) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponseModel(
                        HttpStatus.BAD_REQUEST,
                        HttpStatus.BAD_REQUEST.value(),
                        Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()
                ));
    }

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ErrorResponseModel> handleErrorResponseException(ErrorResponseException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(new ErrorResponseModel(
                        HttpStatus.valueOf(e.getStatusCode().value()),
                        e.getStatusCode().value(),
                        HttpStatus.valueOf(e.getStatusCode().value()).name()
                ));
    }
}