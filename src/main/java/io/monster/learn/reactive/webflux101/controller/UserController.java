package io.monster.learn.reactive.webflux101.controller;

import io.monster.learn.reactive.webflux101.resource.User;
import io.monster.learn.reactive.webflux101.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Flux<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{userId}")
    public Mono<User> getUserById(@PathVariable String userId) {
        logger.debug("UserByIdNonBlockingController - executing on thread: " + Thread.currentThread().getId());
        return userService.getUserById(userId).switchIfEmpty(Mono.error(UserNotFoundException::new));
    }

    @GetMapping("/users/blocking/{userId}")
    public Mono<User> getUserByIdBlocking(@PathVariable String userId) throws Exception {
        logger.debug("UserByIdBlockingController - executing on thread: " + Thread.currentThread().getId());
        return Mono.fromSupplier(() -> userService.getUserByIdBlockingCall(userId)).publishOn(Schedulers.boundedElastic());
    }

    @PostMapping("/users")
    public Mono<User> createUser(@RequestBody User user) {
        return userService.createNewUser(user);
    }

    @GetMapping("/users/age/average")
    public Mono<Double> getAverageAgeOfUsers() {
        return userService.computeAverageAgeOfUsers();
    }

}


class UserNotFoundException extends Throwable {
    private static final Logger logger = LoggerFactory.getLogger(UserNotFoundException.class);

    UserNotFoundException() {
        logger.debug("User not found exception");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}