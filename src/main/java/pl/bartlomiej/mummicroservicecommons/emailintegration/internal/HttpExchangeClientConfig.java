package pl.bartlomiej.mummicroservicecommons.emailintegration.internal;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import pl.bartlomiej.mummicroservicecommons.emailintegration.external.EmailHttpService;

@AutoConfiguration
@ConditionalOnProperty(value = "mum-microservice-commons.email-integration.enabled", havingValue = "true")
class HttpExchangeClientConfig {

    @Bean
    HttpServiceProxyFactory restClientHttpServiceProxyFactory(@Qualifier("emailHttpServiceRestClient") RestClient restClient) {
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        return HttpServiceProxyFactory.builderFor(adapter).build();
    }

    @Bean
    EmailHttpService emailHttpService(HttpServiceProxyFactory factory) {
        return factory.createClient(EmailHttpService.class);
    }
}
