package ru.practicum.stats.endpointhit.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ViewStats {
    String app;
    String uri;
    Long hits;
}
