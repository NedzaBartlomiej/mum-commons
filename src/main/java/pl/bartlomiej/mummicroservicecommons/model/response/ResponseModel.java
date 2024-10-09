package pl.bartlomiej.mummicroservicecommons.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel<T> {

    private final HttpStatus httpStatus;
    private final Integer httpStatusCode;
    private final LocalDateTime time = LocalDateTime.now();

    private final String message;
    private final T body;

    private ResponseModel(Builder<T> builder) {
        this.httpStatus = builder.httpStatus;
        this.httpStatusCode = builder.httpStatusCode;
        this.message = builder.message;
        this.body = builder.body;
    }

    public static class Builder<T> {

        private final HttpStatus httpStatus;
        private final int httpStatusCode;

        private String message;
        private T body;

        public Builder(HttpStatus httpStatus, int httpStatusCode) {
            this.httpStatus = httpStatus;
            this.httpStatusCode = httpStatusCode;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> body(T body) {
            this.body = body;
            return this;
        }

        public ResponseModel<T> build() {
            return new ResponseModel<>(this);
        }
    }
}