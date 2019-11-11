package io.monster.learn.reactive.webflux101.service;

import io.monster.learn.reactive.webflux101.persistence.UserRepository;
import io.monster.learn.reactive.webflux101.resource.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<Double> computeAverageAgeOfUsers() {
//        .toStream is a Blocking Call and errors out
//        double average = userRepository.findAll().toStream().mapToInt(User::getAge).average().orElseGet(() -> 0.0);
//        return CompletableFuture.completedFuture(average);
        return userRepository.findAll().collectList().map(this::calculateAverage);
    }

    private Double calculateAverage(List<User> users) {
        return users.stream().mapToInt(User::getAge).average().orElseGet(() -> 0.0);
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    public Mono<User> createNewUser(User user) {
        return userRepository.save(user);
    }

}
