package pl.bartlomiej.mumcommons.core.webtools.requestinterceptor.authorizedinterceptor;

public interface AuthorizedInterceptorTokenManager {
    String getToken();

    void refreshToken();
}
