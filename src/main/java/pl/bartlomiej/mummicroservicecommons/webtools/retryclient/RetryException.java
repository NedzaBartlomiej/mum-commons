package pl.bartlomiej.mummicroservicecommons.webtools.retryclient;

public class RetryException extends RuntimeException {
    public RetryException(String message, Throwable cause) {
        super(message, cause);
    }
}