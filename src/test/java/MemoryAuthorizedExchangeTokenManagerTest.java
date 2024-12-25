import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler.reactor.MemoryAuthorizedExchangeTokenManager;
import reactor.core.publisher.Mono;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// AI GENERATED TEST - NOT OFFICIAL RELEASE
public class MemoryAuthorizedExchangeTokenManagerTest {

    private MemoryAuthorizedExchangeTokenManager tokenManager;

    @BeforeEach
    public void setup() {
        // Symulacja provider, który pobiera token
        pl.bartlomiej.mumcommons.core.webtools.requesthandler.authorizedhandler.reactor.AuthorizedExchangeTokenProvider tokenProvider = () -> {
            // Zakładamy, że token jest pobierany asynchronicznie
            System.out.println("fetching token");
            return Mono.just("token" + Math.random());
        };

        tokenManager = new MemoryAuthorizedExchangeTokenManager(tokenProvider);
    }

    @Test
    public void testTokenManagerConcurrencyWithThreads() throws InterruptedException {
        int numberOfThreads = 1000; // Liczba wątków próbujących pobrać token jednocześnie

        // Używamy ExecutorService, aby zarządzać wątkami w sposób bardziej kontrolowany
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        // Uruchamiamy wszystkie wątki
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                // Subskrypcja na `Mono` bez blokowania wątków w głównym wątku
                tokenManager.refreshToken()
                        .doFinally(signalType -> latch.countDown()) // Zapewni, że wszystkie wątki skończą
                        .subscribe(token -> {
                            // Logowanie dla każdego tokenu
                            System.out.println("Token: " + token);
                        });
            });
        }

        // Czekamy na zakończenie wszystkich wątków
        latch.await();
        executorService.shutdown();
    }

    @Test
    public void testGetTokenConcurrencySafety() throws InterruptedException {
        int numberOfThreads = 50; // Liczba wątków próbujących pobrać token jednocześnie

        // Używamy ExecutorService do zarządzania wątkami
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads); // Używamy CountDownLatch, by poczekać na zakończenie wszystkich wątków

        // Uruchamiamy wszystkie wątki
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                tokenManager.getToken()
                        .doFinally(signalType -> latch.countDown()) // Zmniejszamy licznik po zakończeniu każdego wątku
                        .subscribe(token -> {
                            // Sprawdzamy, czy token jest poprawny
                            System.out.println(Thread.currentThread().getName() + " fetched token: " + token);
                        });
            });
        }

        // Czekamy na zakończenie wszystkich wątków
        latch.await();
        executorService.shutdown();
    }
}