package ru.practicum.stats.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ViewStatsDto {
    String app;
    String uri;
    Long hits;
}
