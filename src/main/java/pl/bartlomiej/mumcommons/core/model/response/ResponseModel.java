package pl.bartlomiej.mumcommons.core.model.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = ResponseModel.Builder.class)
public class ResponseModel<T> {

    private final HttpStatus httpStatus;
    private final int httpStatusCode;
    private final LocalDateTime time = LocalDateTime.now();
    private final boolean success;
    private final String message;
    private final T body;

    private ResponseModel(Builder<T> builder) {
        this.httpStatus = builder.httpStatus;
        this.httpStatusCode = builder.httpStatusCode;
        this.message = builder.message;
        this.body = builder.body;
        success = builder.success;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder<T> {

        private final HttpStatus httpStatus;
        private final int httpStatusCode;
        private final boolean success;
        private String message;
        private T body;

        @JsonCreator
        public Builder(@JsonProperty("httpStatus") HttpStatus httpStatus,
                       @JsonProperty("success") boolean success) {
            this.httpStatus = httpStatus;
            this.httpStatusCode = httpStatus.value();
            this.success = success;
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

    public static ResponseModel<Void> buildBasicErrorResponseModel(HttpStatus httpStatus, String errMessage) {
        return new Builder<Void>(httpStatus, false).message(errMessage).build();
    }
}