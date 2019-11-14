package io.monster.learn.reactive.webflux101.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobEvent {

    private EventType eventType;
    private LocalDateTime eventTime;

}

