package pl.bartlomiej.mumcommons.emailintegration.internal;

import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import pl.bartlomiej.mumcommons.coreutils.webtools.requesthandler.authorizedhandler.*;

@AutoConfiguration
@ConditionalOnProperty(value = "mum-commons.email-integration.enabled", havingValue = "true")
class KeycloakAuthorizedRestClientConfig {

    private final String emailServiceUrl;

    KeycloakAuthorizedRestClientConfig(@Value("${mum-commons.email-integration.email-service-url}") String emailServiceUrl) {
        this.emailServiceUrl = emailServiceUrl;
    }

    @Bean
    RestClient emailServiceAuthorizedRestClient(@Qualifier("emailAuthorizedRequestInterceptor") ClientHttpRequestInterceptor interceptor) {
        return RestClient.builder()
                .baseUrl(emailServiceUrl)
                .requestInterceptor(interceptor)
                .build();
    }

    @Bean
    ClientHttpRequestInterceptor emailAuthorizedRequestInterceptor(@Qualifier("emailAuthorizedClientTokenManager") AuthorizedInterceptorTokenManager tokenManager) {
        return new AuthorizedRequestInterceptor(tokenManager);
    }

    @Bean
    AuthorizedInterceptorTokenManager emailAuthorizedClientTokenManager(@Qualifier("emailAuthorizedClientTokenProvider") AuthorizedInterceptorTokenProvider tokenProvider) {
        return new MemoryAuthorizedInterceptorTokenManager(tokenProvider);
    }

    @Bean
    AuthorizedInterceptorTokenProvider emailAuthorizedClientTokenProvider(Keycloak keycloak) {
        return new KeycloakAuthorizedInterceptorTokenProvider(keycloak.tokenManager());
    }
}