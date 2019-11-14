package io.monster.learn.reactive.webflux101.controller;

import io.monster.learn.reactive.webflux101.resource.Country;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.Collections;

@RestController
@RequestMapping("/api")
public class CountriesController {

    private WebClient webClient;

    public CountriesController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/countries")
    public Flux<Country> getAllCountries() {
        return webClient
                    .get()
                    .uri("https://restcountries.eu/rest/v2/all")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(Country.class)
//                    .onErrorResume(e -> Flux.error(() -> new RuntimeException("Remote request errored out: " + e.getMessage())));
                    .onErrorReturn(new Country("Australia", "Canberra", "Oceania", BigInteger.ONE, Collections.singletonList("AEST")));
    }

    @GetMapping(value = "/countries/{countryName}")
    public Mono<Country> getCurrentCountry(@PathVariable String countryName) {
        Country defaultCountry = new Country("Australia", "Canberra", "Oceania", BigInteger.ONE, Collections.singletonList("AEST"));
        return webClient.get()
                .uri("https://restcountries.eu/rest/v2/al")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> throwInvalidUrlException())
                .bodyToFlux(Country.class)
                .filter(c -> countryName.equalsIgnoreCase(c.getName()))
                .next()
                .defaultIfEmpty(defaultCountry);
    }

    private Mono<? extends Throwable> throwInvalidUrlException() {
        return Mono.error(new InvalidUrlException("Invalid URL"));
    }


}

class InvalidUrlException extends RuntimeException {
    public InvalidUrlException() {
    }

    public InvalidUrlException(String message) {
        super(message);
    }
}