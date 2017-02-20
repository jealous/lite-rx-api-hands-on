package io.pivotal.literx;

import java.time.Duration;
import java.util.Arrays;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Learn how to create Flux instances.
 *
 * @author Sebastien Deleuze
 * @see <a href="http://projectreactor.io/core/docs/api/reactor/core/publisher/Flux.html">Flux Javadoc</a>
 */
public class Part01Flux {

//========================================================================================

    @Test
    public void empty() {
        Flux<String> flux = emptyFlux();

        StepVerifier.create(flux)
                .expectComplete()
                .verify();
    }

    private Flux<String> emptyFlux() {
        return Flux.empty();
    }

//========================================================================================

    @Test
    public void fromValues() {
        Flux<String> flux = fooBarFluxFromValues();
        StepVerifier.create(flux)
                .expectNext("foo", "bar")
                .expectComplete()
                .verify();
    }

    private Flux<String> fooBarFluxFromValues() {
        return Flux.just("foo", "bar");
    }

//========================================================================================

    @Test
    public void fromList() {
        Flux<String> flux = fooBarFluxFromList();
        StepVerifier.create(flux)
                .expectNext("foo", "bar")
                .expectComplete()
                .verify();
    }

    private Flux<String> fooBarFluxFromList() {
        return Flux.fromIterable(Arrays.asList("foo", "bar"));
    }

//========================================================================================

    @Test
    public void error() {
        Flux<String> flux = errorFlux();
        StepVerifier.create(flux)
                .expectError(IllegalStateException.class)
                .verify();
    }

    private Flux<String> errorFlux() {
        return Flux.error(new IllegalStateException());
    }

//========================================================================================

    @Test
    public void countEach100ms() {
        Flux<Long> flux = counter();
        StepVerifier.create(flux)
                .expectNext(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L)
                .expectComplete()
                .verify();
    }

    private Flux<Long> counter() {
        return Flux.interval(Duration.ofMillis(100)).take(10L);
    }

}
