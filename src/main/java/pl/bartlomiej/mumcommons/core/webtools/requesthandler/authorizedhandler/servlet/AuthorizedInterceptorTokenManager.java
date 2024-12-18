package pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler.servlet;

public interface AuthorizedInterceptorTokenManager {
    String getToken();

    void refreshToken();
}
