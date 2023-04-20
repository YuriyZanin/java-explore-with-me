package ru.practicum.stats.client.model;

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
