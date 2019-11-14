package io.monster.learn.reactive.webflux101.service;

import io.monster.learn.reactive.webflux101.persistence.UserRepository;
import io.monster.learn.reactive.webflux101.resource.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<Double> computeAverageAgeOfUsers() {
//        .toStream is a Blocking Call and errors out
//        double average = userRepository.findAll().toStream().mapToInt(User::getAge).average().orElseGet(() -> 0.0);
//        return CompletableFuture.completedFuture(average);
        return userRepository.findAll()
                             .collectList()
                             .map(this::calculateAverage);
    }

    private Double calculateAverage(List<User> users) {
        return users
                .stream()
                .mapToInt(User::getAge)
                .average()
                .orElseGet(() -> 0.0);
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> getUserById(String userId) {
        logger.debug("UserByIdNonBlockingService - executing on thread: " + Thread.currentThread().getId());
        return userRepository.findById(userId);
    }

    public User getUserByIdBlockingCall(String userId) {
        logger.debug("UserByIdBlockingService - executing on thread: " + Thread.currentThread().getId());
        User blockingUser = new User(userId, "BlockingUser", 50, 1000.00);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            logger.debug("Interrupted...");
            return blockingUser;
        }
        return blockingUser;
    }

    public Mono<User> createNewUser(User user) {
        return userRepository.save(user);
    }

}
