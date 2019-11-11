package io.monster.learn.reactive.webflux101.resource;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
public class Country {

    private String name;
    private String capital;
    private String region;
    private BigInteger population;
    private List<String> timezones;

}
