package pl.bartlomiej.mummicroservicecommons.exceptionhandling.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.reactor.DefaultErrorResponseModelServerAccessDeniedHandler;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.reactor.DefaultErrorResponseModelServerAuthEntryPoint;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.reactor.ErrorResponseModelServerExceptionHandler;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.reactor.ErrorResponseModelWebExceptionHandler;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.servlet.DefaultErrorResponseModelAccessDeniedHandler;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.servlet.DefaultErrorResponseModelAuthEntryPoint;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.servlet.ErrorResponseModelErrorController;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.servlet.ErrorResponseModelExceptionHandler;
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

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "mum-microservice-commons.exception-handling.type", havingValue = "servlet")
    ErrorResponseModelExceptionHandler errorResponseModelExceptionHandler(ObjectMapper objectMapper, GlobalHttpStatusResolver httpStatusResolver) {
        return new ErrorResponseModelExceptionHandler(objectMapper, httpStatusResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "mum-microservice-commons.exception-handling.type", havingValue = "reactor")
    ErrorResponseModelServerExceptionHandler errorResponseModelServerExceptionHandler(ObjectMapper objectMapper,
                                                                                      GlobalHttpStatusResolver globalHttpStatusResolver) {
        return new ErrorResponseModelServerExceptionHandler(objectMapper, globalHttpStatusResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "mum-microservice-commons.exception-handling.type", havingValue = "servlet")
    DefaultErrorResponseModelAuthEntryPoint defaultErrorResponseModelAuthEntryPoint(ErrorResponseModelExceptionHandler exceptionHandler) {
        return new DefaultErrorResponseModelAuthEntryPoint(exceptionHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "mum-microservice-commons.exception-handling.type", havingValue = "servlet")
    DefaultErrorResponseModelAccessDeniedHandler defaultErrorResponseModelAccessDeniedHandler(ErrorResponseModelExceptionHandler exceptionHandler) {
        return new DefaultErrorResponseModelAccessDeniedHandler(exceptionHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "mum-microservice-commons.exception-handling.type", havingValue = "reactor")
    DefaultErrorResponseModelServerAuthEntryPoint defaultErrorResponseModelServerAuthEntryPoint(ErrorResponseModelServerExceptionHandler exceptionHandler) {
        return new DefaultErrorResponseModelServerAuthEntryPoint(exceptionHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "mum-microservice-commons.exception-handling.type", havingValue = "reactor")
    DefaultErrorResponseModelServerAccessDeniedHandler defaultErrorResponseModelServerAccessDeniedHandler(ErrorResponseModelServerExceptionHandler exceptionHandler) {
        return new DefaultErrorResponseModelServerAccessDeniedHandler(exceptionHandler);
    }
}