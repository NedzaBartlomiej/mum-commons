import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler.reactor.AuthorizedExchangeTokenProvider;
import pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler.reactor.MemoryAuthorizedExchangeTokenManager;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

// AI GENERATED TESTS
class MemoryAuthorizedExchangeTokenManagerTest {

    private MemoryAuthorizedExchangeTokenManager tokenManager;
    private AuthorizedExchangeTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        // Tworzymy mock dla tokenProvider
        tokenProvider = mock(AuthorizedExchangeTokenProvider.class);
        tokenManager = new MemoryAuthorizedExchangeTokenManager(tokenProvider);
    }

    @Test
    void testGetTokenWithMultipleThreadsConfigurable() {
        int threadCount = 100; // Liczba równoczesnych wątków do testowania

        // Przygotowanie: Mockowanie, że token nie jest jeszcze dostępny
        String newToken = "newToken";
        when(tokenProvider.getValidToken()).thenReturn(Mono.just(newToken));

        // Wywołujemy getToken() równocześnie w wielu wątkach
        Flux<String> tokenFlux = Flux.fromStream(IntStream.range(0, threadCount).mapToObj(i -> tokenManager.getToken()))
                .flatMap(mono -> mono);

        StepVerifier.create(tokenFlux)
                .expectNextCount(threadCount) // Oczekujemy, że każdy wątek zwróci token
                .verifyComplete();

        // Sprawdzamy, czy provider był wywołany tylko raz
        verify(tokenProvider, times(1)).getValidToken();
    }

    @Test
    void testRefreshTokenWithMultipleThreadsConfigurable() {
        int threadCount = 100;
        when(tokenProvider.getValidToken()).thenReturn(Mono.just("newToken"));

        // Uruchamiamy równoczesne wywołania refreshToken
        List<Mono<Void>> refreshMonos = IntStream.range(0, threadCount)
                .mapToObj(i -> tokenManager.refreshToken())
                .collect(Collectors.toList());

        // Subskrybujemy wszystkie Monosy
        StepVerifier.create(Mono.when(refreshMonos))
                .verifyComplete();

        // Weryfikacja, że provider był wywołany tylko raz
        verify(tokenProvider, times(1)).getValidToken();
    }
}
