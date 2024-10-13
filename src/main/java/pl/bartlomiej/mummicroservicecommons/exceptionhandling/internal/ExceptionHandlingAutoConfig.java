package pl.bartlomiej.mummicroservicecommons.exceptionhandling.internal;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.ErrorResponseModelGlobalRestControllerAdvice;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.statusresolution.DefaultErrorStatusOptionProvider;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.statusresolution.ErrorStatusOptionProvider;
import pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.statusresolution.GlobalHttpStatusResolver;

@AutoConfiguration
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
}