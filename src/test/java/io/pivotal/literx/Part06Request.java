package io.pivotal.literx;

import io.pivotal.literx.domain.User;
import io.pivotal.literx.repository.ReactiveRepository;
import io.pivotal.literx.repository.ReactiveUserRepository;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Learn how to control the demand.
 *
 * @author Sebastien Deleuze
 */
public class Part06Request {

    ReactiveRepository<User> repository = new ReactiveUserRepository();

//========================================================================================

    @Test
    public void requestAll() {
        Flux<User> flux = repository.findAll();
        StepVerifier verifier = requestAllExpectFour(flux);
        verifier.verify();
    }

    private StepVerifier requestAllExpectFour(Flux<User> flux) {
        return StepVerifier.create(flux).expectNextCount(4).expectComplete();
    }

//========================================================================================

    @Test
    public void requestOneByOne() {
        Flux<User> flux = repository.findAll();
        StepVerifier verifier = requestOneExpectSkylerThenRequestOneExpectJesse(flux);
        verifier.verify();
    }

    private StepVerifier requestOneExpectSkylerThenRequestOneExpectJesse(Flux<User> flux) {
        return StepVerifier.create(flux)
                .expectNext(User.SKYLER)
                .expectNext(User.JESSE)
                .thenCancel();
    }

//========================================================================================

    @Test
    public void experimentWithLog() {
        Flux<User> flux = fluxWithLog();
        StepVerifier.create(flux, 0)
                .thenRequest(1)
                .expectNextMatches(u -> true)
                .thenRequest(1)
                .expectNextMatches(u -> true)
                .thenRequest(2)
                .expectNextMatches(u -> true)
                .expectNextMatches(u -> true)
                .expectComplete()
                .verify();
    }

    private Flux<User> fluxWithLog() {
        return repository.findAll().log();
    }


//========================================================================================

    @Test
    public void experimentWithDoOn() {
        Flux<User> flux = fluxWithDoOnPrintln();
        StepVerifier.create(flux)
                .expectNextCount(4)
                .expectComplete()
                .verify();
    }

    private Flux<User> fluxWithDoOnPrintln() {
        return repository.findAll()
                .doOnSubscribe(s -> System.out.println("Starring:"))
                .doOnNext(user -> System.out.println(String.format("%s %s", user.getFirstname(), user.getLastname())))
                .doOnComplete(() -> System.out.println("The end!"));
    }

}
