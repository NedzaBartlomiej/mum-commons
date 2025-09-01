package pl.bartlomiej.mumcommons.coreutils.exceptionhandling.external;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.NoContentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import pl.bartlomiej.mumcommons.coreutils.model.response.ResponseModel;

import java.util.Objects;

@RestControllerAdvice
public class ResponseModelGlobalRestControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<Void> handleNoContentException() {
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseModel<Void>> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.badRequest()
                .body(ResponseModel.buildBasicErrorResponseModel(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ResponseModel<Void>> handleValidationException(BindingResult bindingResult) {
        return ResponseEntity.badRequest()
                .body(ResponseModel.buildBasicErrorResponseModel(HttpStatus.BAD_REQUEST,
                        Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage())
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseModel<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .body(ResponseModel.buildBasicErrorResponseModel(HttpStatus.BAD_REQUEST,
                        Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage())
                );
    }

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ResponseModel<Void>> handleErrorResponseException(ErrorResponseException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(ResponseModel.buildBasicErrorResponseModel(
                        HttpStatus.valueOf(e.getStatusCode().value()),
                        HttpStatus.valueOf(e.getStatusCode().value()).name())
                );
    }
}