package pl.bartlomiej.mumcommons.core.exceptionhandling.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.server.WebExceptionHandler;
import pl.bartlomiej.mumcommons.core.exceptionhandling.external.ResponseModelGlobalRestControllerAdvice;
import pl.bartlomiej.mumcommons.core.exceptionhandling.external.reactor.DefaultResponseModelServerAccessDeniedHandler;
import pl.bartlomiej.mumcommons.core.exceptionhandling.external.reactor.DefaultResponseModelServerAuthEntryPoint;
import pl.bartlomiej.mumcommons.core.exceptionhandling.external.reactor.ResponseModelServerExceptionHandler;
import pl.bartlomiej.mumcommons.core.exceptionhandling.external.reactor.ResponseModelWebExceptionHandler;
import pl.bartlomiej.mumcommons.core.exceptionhandling.external.servlet.DefaultResponseModelAccessDeniedHandler;
import pl.bartlomiej.mumcommons.core.exceptionhandling.external.servlet.DefaultResponseModelAuthEntryPoint;
import pl.bartlomiej.mumcommons.core.exceptionhandling.external.servlet.ResponseModelErrorController;
import pl.bartlomiej.mumcommons.core.exceptionhandling.external.servlet.ResponseModelExceptionHandler;
import pl.bartlomiej.mumcommons.core.exceptionhandling.external.statusresolution.DefaultErrorStatusOptionProvider;
import pl.bartlomiej.mumcommons.core.exceptionhandling.external.statusresolution.ErrorStatusOptionProvider;
import pl.bartlomiej.mumcommons.core.exceptionhandling.external.statusresolution.GlobalHttpStatusResolver;

@ConditionalOnProperty(value = "mum-commons.core.exception-handling.enabled", havingValue = "true")
@AutoConfiguration(before = {ErrorMvcAutoConfiguration.class})
class ExceptionHandlingAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    GlobalHttpStatusResolver globalHttpStatusResolver(ErrorStatusOptionProvider errorStatusOptionProvider) {
        return new DefaultGlobalHttpStatusResolver(errorStatusOptionProvider);
    }

    @Bean
    ResponseModelGlobalRestControllerAdvice responseModelGlobalRestControllerAdvice() {
        return new ResponseModelGlobalRestControllerAdvice();
    }

    @Bean
    @ConditionalOnMissingBean
    ErrorStatusOptionProvider errorStatusOptionProvider() {
        return new DefaultErrorStatusOptionProvider();
    }

    @Bean
    @ConditionalOnProperty(value = "mum-commons.core.exception-handling.type", havingValue = "servlet")
    ResponseModelErrorController responseModelErrorController() {
        return new ResponseModelErrorController();
    }

    @Bean
    @ConditionalOnProperty(value = "mum-commons.core.exception-handling.type", havingValue = "reactor")
    WebExceptionHandler webExceptionHandler(ErrorAttributes errorAttributes,
                                            WebProperties webProperties,
                                            ApplicationContext applicationContext,
                                            ServerCodecConfigurer serverCodecConfigurer) {
        return new ResponseModelWebExceptionHandler(errorAttributes, webProperties, applicationContext, serverCodecConfigurer);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "mum-commons.core.exception-handling.type", havingValue = "servlet")
    ResponseModelExceptionHandler responseModelExceptionHandler(ObjectMapper objectMapper, GlobalHttpStatusResolver httpStatusResolver) {
        return new ResponseModelExceptionHandler(objectMapper, httpStatusResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "mum-commons.core.exception-handling.type", havingValue = "reactor")
    ResponseModelServerExceptionHandler responseModelServerExceptionHandler(ObjectMapper objectMapper,
                                                                            GlobalHttpStatusResolver globalHttpStatusResolver) {
        return new ResponseModelServerExceptionHandler(objectMapper, globalHttpStatusResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "mum-commons.core.exception-handling.type", havingValue = "servlet")
    DefaultResponseModelAuthEntryPoint defaultResponseModelAuthEntryPoint(ResponseModelExceptionHandler exceptionHandler) {
        return new DefaultResponseModelAuthEntryPoint(exceptionHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "mum-commons.core.exception-handling.type", havingValue = "servlet")
    DefaultResponseModelAccessDeniedHandler defaultResponseModelAccessDeniedHandler(ResponseModelExceptionHandler exceptionHandler) {
        return new DefaultResponseModelAccessDeniedHandler(exceptionHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "mum-commons.core.exception-handling.type", havingValue = "reactor")
    DefaultResponseModelServerAuthEntryPoint defaultResponseModelServerAuthEntryPoint(ResponseModelServerExceptionHandler exceptionHandler) {
        return new DefaultResponseModelServerAuthEntryPoint(exceptionHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "mum-commons.core.exception-handling.type", havingValue = "reactor")
    DefaultResponseModelServerAccessDeniedHandler defaultResponseModelServerAccessDeniedHandler(ResponseModelServerExceptionHandler exceptionHandler) {
        return new DefaultResponseModelServerAccessDeniedHandler(exceptionHandler);
    }
}