package pl.bartlomiej.mummicroservicecommons.exceptionhandling.external;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponseModel {
    private final HttpStatus httpStatus;
    private final int httpStatusCode;
    private final String errMessage;
    private final LocalDateTime time = LocalDateTime.now();

    public ErrorResponseModel(HttpStatus httpStatus, int httpStatusCode, String errMessage) {
        this.httpStatus = httpStatus;
        this.httpStatusCode = httpStatusCode;
        this.errMessage = errMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
