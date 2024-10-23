package pl.bartlomiej.mummicroservicecommons.exceptionhandling.internal;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.server.WebExceptionHandler;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.ErrorResponseModelGlobalRestControllerAdvice;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.reactor.ErrorResponseModelWebExceptionHandler;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.servlet.ErrorResponseModelErrorController;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.statusresolution.DefaultErrorStatusOptionProvider;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.statusresolution.ErrorStatusOptionProvider;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.statusresolution.GlobalHttpStatusResolver;

@AutoConfiguration(before = {ErrorMvcAutoConfiguration.class})
class ExceptionHandlingAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    GlobalHttpStatusResolver globalHttpStatusResolver(ErrorStatusOptionProvider errorStatusOptionProvider) {
        return new DefaultGlobalHttpStatusResolver(errorStatusOptionProvider);
    }

    @Bean
    ErrorResponseModelGlobalRestControllerAdvice errorResponseModelGlobalRestControllerAdvice() {
        return new ErrorResponseModelGlobalRestControllerAdvice();
    }

    @Bean
    @ConditionalOnMissingBean
    ErrorStatusOptionProvider errorStatusOptionProvider() {
        return new DefaultErrorStatusOptionProvider();
    }

    @Bean
    @ConditionalOnProperty(value = "mum-microservice-commons.exception-handling.type", havingValue = "servlet")
    ErrorController errorController() {
        return new ErrorResponseModelErrorController();
    }

    @Bean
    @ConditionalOnProperty(value = "mum-microservice-commons.exception-handling.type", havingValue = "reactor")
    WebExceptionHandler webExceptionHandler(ErrorAttributes errorAttributes,
                                            WebProperties webProperties,
                                            ApplicationContext applicationContext,
                                            ServerCodecConfigurer serverCodecConfigurer) {
        return new ErrorResponseModelWebExceptionHandler(errorAttributes, webProperties, applicationContext, serverCodecConfigurer);
    }
}