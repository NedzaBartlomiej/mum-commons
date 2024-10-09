package pl.bartlomiej.mummicroservicecommons.offsettransaction.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class ReactiveOffsetTransactionOperator {
    private static final Logger log = LoggerFactory.getLogger(ReactiveOffsetTransactionOperator.class);

    public static <PerfT, CompT> Mono<Void> performOffsetConsumerTransaction(final PerfT perfFuncArg,
                                                                             final CompT compFuncArg,
                                                                             final Function<PerfT, Mono<Void>> performAction,
                                                                             final Function<CompT, Mono<Void>> compensationAction) {
        return performOffsetTransaction(
                performAction.apply(perfFuncArg),
                compFuncArg,
                compensationAction
        ).then();
    }

    public static <PerfT, PerfR, CompT> Mono<PerfR> performOffsetFunctionTransaction(final PerfT perfFuncArg,
                                                                                     final CompT compFuncArg,
                                                                                     final Function<PerfT, Mono<PerfR>> performAction,
                                                                                     final Function<CompT, Mono<Void>> compensationAction) {
        return performOffsetTransaction(
                performAction.apply(perfFuncArg),
                compFuncArg,
                compensationAction
        );
    }

    private static <PerfR, CompT> Mono<PerfR> performOffsetTransaction(Mono<PerfR> performAction,
                                                                       final CompT compFuncArg,
                                                                       final Function<CompT, Mono<Void>> compensationAction) {
        log.debug("Offset transaction has been initiated.");
        return performAction
                .doOnNext(result -> log.debug("Performing transactional processes."))
                .doOnError(e -> {
                    log.error("Something went wrong in the transaction, invoking compensation functions.", e);
                    compensationAction.apply(compFuncArg)
                            .doOnError(compError -> log.error("Error during compensation process.", compError))
                            .subscribe();
                })
                .onErrorMap(e -> new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, e));
    }
}