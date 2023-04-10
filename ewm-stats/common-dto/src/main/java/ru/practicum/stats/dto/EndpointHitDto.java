package ru.practicum.stats.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class EndpointHitDto {
    Long id;
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;
}
