package pl.bartlomiej.mumcommons.emailintegration.internal;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import pl.bartlomiej.mumcommons.emailintegration.external.EmailHttpService;

@AutoConfiguration
@ConditionalOnProperty(value = "mum-commons.email-integration.enabled", havingValue = "true")
class HttpInterfaceConfig {

    @Bean
    HttpServiceProxyFactory emailServiceFactory(@Qualifier("emailServiceRestClient") RestClient restClient) {
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        return HttpServiceProxyFactory.builderFor(adapter).build();
    }

    @Bean
    EmailHttpService emailHttpService(@Qualifier("emailServiceFactory") HttpServiceProxyFactory factory) {
        return factory.createClient(EmailHttpService.class);
    }
}