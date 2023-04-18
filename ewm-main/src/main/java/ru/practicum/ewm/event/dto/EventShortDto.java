package ru.practicum.ewm.event.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserShortDto;

@Value
@Builder
public class EventShortDto {
    Long id;
    String annotation;
    CategoryDto category;
    int confirmedRequests;
    String eventDate;
    UserShortDto initiator;
    Boolean paid;
    String title;
    int views;
}
