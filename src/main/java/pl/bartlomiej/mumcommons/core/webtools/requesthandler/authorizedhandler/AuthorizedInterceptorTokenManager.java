package pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler;

public interface AuthorizedInterceptorTokenManager {
    String getToken();

    void refreshToken();
}
