package pl.bartlomiej.mumcommons.coreutils.webtools.requesthandler.authorizedhandler;

public interface AuthorizedInterceptorTokenManager {
    String getToken();

    void refreshToken();
}
