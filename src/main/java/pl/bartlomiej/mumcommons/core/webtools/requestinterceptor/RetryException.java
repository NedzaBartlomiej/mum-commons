package pl.bartlomiej.mumcommons.core.webtools.requestinterceptor;

public class RetryException extends RuntimeException {
    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }
}