package pl.bartlomiej.mumcommons.coreutils.exceptionhandling.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import pl.bartlomiej.mumcommons.coreutils.exceptionhandling.external.*;
import pl.bartlomiej.mumcommons.coreutils.exceptionhandling.external.statusresolution.DefaultErrorStatusOptionProvider;
import pl.bartlomiej.mumcommons.coreutils.exceptionhandling.external.statusresolution.ErrorStatusOptionProvider;
import pl.bartlomiej.mumcommons.coreutils.exceptionhandling.external.statusresolution.GlobalHttpStatusResolver;

@ConditionalOnProperty(value = "mum-commons.coreutils.exception-handling.enabled", havingValue = "true")
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
    ResponseModelErrorController responseModelErrorController() {
        return new ResponseModelErrorController();
    }

    @Bean
    @ConditionalOnMissingBean
    ResponseModelExceptionHandler responseModelExceptionHandler(ObjectMapper objectMapper, GlobalHttpStatusResolver httpStatusResolver) {
        return new ResponseModelExceptionHandler(objectMapper, httpStatusResolver);
    }

    @Bean
    @ConditionalOnMissingBean
    DefaultResponseModelAuthEntryPoint defaultResponseModelAuthEntryPoint(ResponseModelExceptionHandler exceptionHandler) {
        return new DefaultResponseModelAuthEntryPoint(exceptionHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    DefaultResponseModelAccessDeniedHandler defaultResponseModelAccessDeniedHandler(ResponseModelExceptionHandler exceptionHandler) {
        return new DefaultResponseModelAccessDeniedHandler(exceptionHandler);
    }
}