package ru.practicum.ewm.event.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.ewm.validation.NotSoonerThan2Hours;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Value
@Builder
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;
    @NotNull
    Long categoryId;
    @NotBlank
    @Size(min = 20, max = 7000)
    String description;
    @NotSoonerThan2Hours
    LocalDateTime eventDate;
    @NotNull
    LocationDto location;
    Boolean paid;
    int participantLimit;
    Boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120)
    String title;
}
