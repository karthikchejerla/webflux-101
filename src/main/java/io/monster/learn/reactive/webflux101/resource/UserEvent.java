package io.monster.learn.reactive.webflux101.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEvent {

    private String name;
    private LocalDateTime eventTime;
}
