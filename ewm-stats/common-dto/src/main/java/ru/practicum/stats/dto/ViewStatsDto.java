package ru.practicum.stats.dto;

import lombok.Value;

@Value
public class ViewStatsDto {
    String app;
    String uri;
    Integer hits;
}
