package io.monster.learn.reactive.webflux101.persistence;

import io.monster.learn.reactive.webflux101.resource.Job;
import io.monster.learn.reactive.webflux101.resource.User;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DatabaseSeeder {

    private static Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    private UserRepository userRepository;
    private JobRepository jobRepository;

    public DatabaseSeeder(UserRepository userRepository, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public static Job[] seedJobsList =
        new Job[]{
                new Job("1", "Java Developer", "The one who can code in Java", LocalDate.now(), LocalDateTime.now()),
                new Job("2", "Scala Developer", "The one who can code in Scala", LocalDate.now(), LocalDateTime.now()),
                new Job("3", "Kotlin Developer", "The one who can code in Kotlin", LocalDate.now(), LocalDateTime.now())
        };

    @EventListener(ApplicationReadyEvent.class)
    public void initialize() throws Exception {
        Publisher<User> userPublisher = Flux.just("John Doe", "Jack Shaw")
                                            .map(name -> new User(UUID.randomUUID().toString(), name, 20, 1000))
                                            .flatMap(u -> this.userRepository.save(u));

        userRepository.deleteAll()
                      .thenMany(userPublisher)
                      .thenMany(userRepository.findAll())
                      .map(User::toString)
                      .subscribe(logger::debug);


        Publisher<Job> jobsPublisher = Flux.just(DatabaseSeeder.seedJobsList).flatMap(job -> jobRepository.save(job));

        jobRepository.deleteAll()
                     .thenMany(jobsPublisher)
                     .thenMany(jobRepository.findAll())
                     .map(Job::toString)
                     .subscribe(logger::debug);
    }
}
