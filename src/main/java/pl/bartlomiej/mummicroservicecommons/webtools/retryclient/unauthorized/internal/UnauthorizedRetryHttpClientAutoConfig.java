package pl.bartlomiej.mummicroservicecommons.webtools.retryclient.unauthorized.internal;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import pl.bartlomiej.mummicroservicecommons.webtools.retryclient.unauthorized.external.RetryClientTokenProvider;

@AutoConfiguration
@ConditionalOnProperty(value = "mum-microservice-commons.web-tools.retry-client.unauthorized.enabled", havingValue = "true")
class UnauthorizedRetryHttpClientAutoConfig {

    @Bean
    @ConditionalOnProperty(value = "mum-microservice-commons.web-tools.retry-client.type", havingValue = "servlet")
    RestClient unauthorizedRetryRestClient(UnauthorizedRetryRequestInterceptor unauthorizedRetryRequestInterceptor) {
        return RestClient.builder()
                .requestInterceptor(unauthorizedRetryRequestInterceptor)
                .build();
    }

    @Bean
    @ConditionalOnProperty(value = "mum-microservice-commons.web-tools.retry-client.type", havingValue = "servlet")
    UnauthorizedRetryRequestInterceptor unauthorizedRetryRequestInterceptor(RetryClientTokenProvider tokenProvider) {
        return new UnauthorizedRetryRequestInterceptor(tokenProvider);
    }

    // todo reactive way
}