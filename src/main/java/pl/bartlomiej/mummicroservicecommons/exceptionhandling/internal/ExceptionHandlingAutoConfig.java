package pl.bartlomiej.mummicroservicecommons.exceptionhandling.internal;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.ErrorResponseModelGlobalRestControllerAdvice;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.GlobalHttpStatusResolver;

@AutoConfiguration
class ExceptionHandlingAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    GlobalHttpStatusResolver globalHttpStatusResolver() {
        return new DefaultGlobalHttpStatusResolver();
    }

    @Bean
    ErrorResponseModelGlobalRestControllerAdvice errorResponseModelGlobalRestControllerAdvice() {
        return new ErrorResponseModelGlobalRestControllerAdvice();
    }
}