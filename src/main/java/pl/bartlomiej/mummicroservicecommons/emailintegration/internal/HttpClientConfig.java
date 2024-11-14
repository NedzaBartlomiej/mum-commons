package pl.bartlomiej.mummicroservicecommons.emailintegration.internal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@AutoConfiguration
@ConditionalOnProperty(value = "mum-microservice-commons.email-integration.enabled", havingValue = "true")
class HttpClientConfig {

    private final String emailServiceUrl;

    HttpClientConfig(@Value("${mum-microservice-commons.email-integration.email-service-url}") String emailServiceUrl) {
        this.emailServiceUrl = emailServiceUrl;
    }

    @Bean
    RestClient emailServiceRestClient() {
        return RestClient.builder()
                .baseUrl(this.emailServiceUrl)
                .build();
    }
}