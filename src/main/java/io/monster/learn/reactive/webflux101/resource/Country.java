package io.monster.learn.reactive.webflux101.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    private String name;
    private String capital;
    private String region;
    private BigInteger population;
    private List<String> timezones;

}
