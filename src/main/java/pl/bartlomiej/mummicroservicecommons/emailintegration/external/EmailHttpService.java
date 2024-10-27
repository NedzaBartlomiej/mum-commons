package pl.bartlomiej.mummicroservicecommons.emailintegration.external;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.PostExchange;
import pl.bartlomiej.mummicroservicecommons.emailintegration.external.model.StandardEmail;

public interface EmailHttpService {

    @PostExchange("/v1/emails/standard")
    void sendStandardEmail(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String serviceAccToken, @RequestBody StandardEmail standardEmail);
}