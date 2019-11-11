package io.monster.learn.reactive.webflux101.controller;

import io.monster.learn.reactive.webflux101.resource.Country;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigInteger;
import java.util.Collections;

@RestController
@RequestMapping("/api")
public class CountriesController {

    private WebClient webClient;

    public CountriesController(WebClient webClient) {
        this.webClient = webClient;
    }

    @RequestMapping("/countries")
    public Flux<Country> getAllCountries() {
        return webClient
                    .get()
                    .uri("https://restcountries.eu/rest/v2/al")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(Country.class)
//                    .onErrorResume(e -> Flux.error(() -> new RuntimeException("Remote request errored out: " + e.getMessage())));
                    .onErrorReturn(new Country("Austalia", "Canberra", "Oceania", BigInteger.ONE, Collections.singletonList("AEST")));
    }


}
