package ru.practicum.ewm.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

import static ru.practicum.ewm.utils.DateTimeUtils.DEFAULT_DATE_TIME_PATTERN;

@Mapper
public interface EventMapper {
    EventMapper MAPPER = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "createdOn", source = "createdOn", dateFormat = DEFAULT_DATE_TIME_PATTERN)
    @Mapping(target = "eventDate", source = "eventDate", dateFormat = DEFAULT_DATE_TIME_PATTERN)
    @Mapping(target = "publishedOn", source = "publishedOn", dateFormat = DEFAULT_DATE_TIME_PATTERN)
    EventFullDto toFullDto(Event event);

    @Mapping(target = "eventDate", source = "eventDate", dateFormat = DEFAULT_DATE_TIME_PATTERN)
    EventShortDto toShortDto(Event event);

    @Mapping(target = "category.id", source = "categoryId")
    Event toEvent(NewEventDto newEventDto);

    List<EventFullDto> toFullDtos(List<Event> events);

    List<EventShortDto> toShortDtos(List<Event> events);
}
