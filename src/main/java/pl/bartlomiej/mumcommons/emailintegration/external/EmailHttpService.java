package pl.bartlomiej.mumcommons.emailintegration.external;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import pl.bartlomiej.mumcommons.emailintegration.external.model.LinkedEmail;
import pl.bartlomiej.mumcommons.emailintegration.external.model.StandardEmail;

public interface EmailHttpService {

    @PostExchange("/v1/emails/standard")
    void sendStandardEmail(@RequestBody StandardEmail standardEmail);

    @PostExchange("/v1/emails/linked")
    void sendLinkedEmail(@RequestBody LinkedEmail linkedEmail);
}