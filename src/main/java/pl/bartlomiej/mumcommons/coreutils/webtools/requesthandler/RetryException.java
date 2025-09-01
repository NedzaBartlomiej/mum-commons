package pl.bartlomiej.mumcommons.coreutils.webtools.requesthandler;

public class RetryException extends RuntimeException {
    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }
}