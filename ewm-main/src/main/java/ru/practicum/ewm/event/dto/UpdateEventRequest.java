package ru.practicum.ewm.event.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.stats.utils.DateTimeUtils;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Value
@Builder
public class UpdateEventRequest {
    @Size(min = 20, max = 2000)
    String annotation;
    Long categoryId;
    @Size(min = 20, max = 7000)
    String description;
    @Pattern(regexp = DateTimeUtils.DEFAULT_DATE_TIME_REGEXP)
    String eventDate;
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    StateAction stateAction;
    @Size(min = 3, max = 120)
    String title;
}
