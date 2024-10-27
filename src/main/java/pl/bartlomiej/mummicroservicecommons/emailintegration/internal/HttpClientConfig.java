package pl.bartlomiej.mummicroservicecommons.emailintegration.internal;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@AutoConfiguration
@ConditionalOnProperty(value = "mum-microservice-commons.email-integration.enabled", havingValue = "true")
class HttpClientConfig {

    @Bean
    RestClient emailHttpServiceRestClient() {
        return RestClient.builder()
                .baseUrl("http://email-service:8084/")
                .build();
    }
}
