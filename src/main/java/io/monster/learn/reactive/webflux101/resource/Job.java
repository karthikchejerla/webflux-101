package io.monster.learn.reactive.webflux101.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    private String id;
    private String title;
    private String description;
    private LocalDate postedOn;
    private LocalDateTime viewedOn;
}
