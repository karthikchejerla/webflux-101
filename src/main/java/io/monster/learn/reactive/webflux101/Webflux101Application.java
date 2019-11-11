package io.monster.learn.reactive.webflux101;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class Webflux101Application {

    public static void main(String[] args) {
        SpringApplication.run(Webflux101Application.class, args);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

}
