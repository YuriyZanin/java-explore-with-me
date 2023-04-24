package ru.practicum.ewm.event.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.stats.utils.DateTimeUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Value
@Builder
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    String annotation;
    @NotNull
    Long category;
    @NotBlank
    @Size(min = 20, max = 7000)
    String description;
    @Pattern(regexp = DateTimeUtils.DEFAULT_DATE_TIME_REGEXP)
    String eventDate;
    @NotNull
    LocationDto location;
    Boolean paid;
    int participantLimit;
    Boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120)
    String title;
}
