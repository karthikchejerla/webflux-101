package io.monster.learn.reactive.webflux101.controller;

import io.monster.learn.reactive.webflux101.resource.User;
import io.monster.learn.reactive.webflux101.resource.UserEvent;
import io.monster.learn.reactive.webflux101.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
public class UserController {

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
        return userService.getUserById(userId);
    }

    @PostMapping("/users")
    public Mono<User> createUser(@RequestBody User user) {
        return userService.createNewUser(user);
    }

    @GetMapping("/users/age/average")
    public Mono<Double> getAverageAgeOfUsers() {
        return userService.computeAverageAgeOfUsers();
    }

    @GetMapping(value = "/freeMemory", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UserEvent> getJvmInfo() {
        return Flux.<UserEvent>generate(sink -> {
            System.out.println(Thread.currentThread().getId());
            sink.next(new UserEvent("test", LocalDateTime.now()));
        })
                .delayElements(Duration.ofSeconds(1));
    }


}
