import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler.reactor.AuthorizedExchangeTokenProvider;
import pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler.reactor.MemoryAuthorizedExchangeTokenManager;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// AI GENERATED TESTS - NOT OFFICIAL RELEASE
class MemoryAuthorizedExchangeTokenManagerTest {

    private MemoryAuthorizedExchangeTokenManager tokenManager;

    @BeforeEach
    void setUp() {
        // Tworzymy mock dla tokenProvider
        AuthorizedExchangeTokenProvider tokenProvider = () -> {
            System.out.println("Fetching token using AuthorizedExchangeTokenProvider mock.");
            System.out.println("Thread: " + Thread.currentThread());
            String token = "token-" + (int) (Math.random() * 100) + 1;
            System.out.println("Returning new token: " + token);
            return Mono.just(token);
        };
        tokenManager = new MemoryAuthorizedExchangeTokenManager(tokenProvider);
    }

    @Test
    void testGetTokenWithMultipleThreadsConfigurable() {
        int threadCount = 10; // Liczba równoczesnych wątków do testowania

        // Wywołujemy getToken() równocześnie w wielu wątkach reaktywnych
        Flux<String> tokenFlux = Flux.fromStream(IntStream.range(0, threadCount).mapToObj(i ->
                        tokenManager.getToken().subscribeOn(Schedulers.parallel())) // Użycie reaktywnych wątków
                )
                .flatMap(mono -> mono)
                .doOnNext(token -> System.out.println("Received token: " + token));  // Wypisujemy tokeny;

        StepVerifier.create(tokenFlux)
                .expectNextCount(threadCount) // Oczekujemy, że każdy wątek zwróci token
                .verifyComplete();
    }

    @Test
    void testRefreshTokenWithMultipleThreadsConfigurable() {
        int threadCount = 10000;

        // Uruchamiamy równoczesne wywołania refreshToken na wątkach reaktywnych
        List<Mono<String>> refreshMonos = IntStream.range(0, threadCount)
                .mapToObj(i -> tokenManager.refreshToken().subscribeOn(Schedulers.parallel())) // Użycie reaktywnych wątków
                .collect(Collectors.toList());

        // Subskrybujemy wszystkie Monosy na wątkach reaktywnych
        StepVerifier.create(Mono.when(refreshMonos))
                .verifyComplete();
    }
}
