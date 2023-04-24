package ru.practicum.ewm.event.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LocationDto {
    Float lat;
    Float lon;
}
