package pl.bartlomiej.mumcommons.core.webtools.retry;

public class RetryException extends RuntimeException {
    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }
}