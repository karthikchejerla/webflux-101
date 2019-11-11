package io.monster.learn.reactive.webflux101.persistence;

import io.monster.learn.reactive.webflux101.resource.User;
import org.reactivestreams.Publisher;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Component
public class DatabaseSeeder {

    private UserRepository userRepository;

    public DatabaseSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initialize() throws Exception {
        Publisher<User> userPublisher = Flux.just("John Doe", "Jack Shaw")
                                            .map(name -> new User(UUID.randomUUID().toString(), name, 20, 1000))
                                            .flatMap(u -> this.userRepository.save(u));

        userRepository.deleteAll()
                      .thenMany(userPublisher)
                      .thenMany(userRepository.findAll())
                      .subscribe(System.out::println);
    }
}
