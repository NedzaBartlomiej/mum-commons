package pl.bartlomiej.mumcommons.emailintegration.internal;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import pl.bartlomiej.mumcommons.core.webtools.retry.unauthorized.KeycloakRetryClientTokenProvider;
import pl.bartlomiej.mumcommons.core.webtools.retry.unauthorized.RetryClientTokenProvider;
import pl.bartlomiej.mumcommons.core.webtools.retry.unauthorized.UnauthorizedRetryRequestInterceptor;
import pl.bartlomiej.mumcommons.globalidmservice.idm.external.keycloakidm.servlet.KeycloakService;

@AutoConfiguration
@ConditionalOnProperty(value = "mum-commons.email-integration.enabled", havingValue = "true")
class HttpClientConfig {

    private final String emailServiceUrl;

    HttpClientConfig(@Value("${mum-commons.email-integration.email-service-url}") String emailServiceUrl) {
        this.emailServiceUrl = emailServiceUrl;
    }

    @Bean
    RestClient emailServiceRestClient(@Qualifier("emailRetryClientTokenProvider") RetryClientTokenProvider tokenProvider) {
        return RestClient.builder()
                .requestInterceptor(new UnauthorizedRetryRequestInterceptor(tokenProvider))
                .baseUrl(emailServiceUrl)
                .build();
    }

    @Bean
    RetryClientTokenProvider emailRetryClientTokenProvider(KeycloakService keycloakService) {
        return new KeycloakRetryClientTokenProvider(keycloakService);
    }
}