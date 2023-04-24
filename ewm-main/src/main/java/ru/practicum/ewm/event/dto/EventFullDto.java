package ru.practicum.ewm.event.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserShortDto;

@Value
@Builder
public class EventFullDto {
    Long id;
    String annotation;
    CategoryDto category;
    int confirmedRequests;
    String createdOn;
    String description;
    String eventDate;
    UserShortDto initiator;
    LocationDto location;
    Boolean paid;
    int participantLimit;
    String publishedOn;
    Boolean requestModeration;
    String state;
    String title;
    int views;
}
