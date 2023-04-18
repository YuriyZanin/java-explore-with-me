package ru.practicum.ewm.event.model;

import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Embeddable
public class Location {
    private Float lat;
    private Float lon;
}
