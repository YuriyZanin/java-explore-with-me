package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.Collection;

public interface EventService {
    Collection<EventFullDto> getAll(EventRequestParamsDto params);

    EventFullDto updateByAdmin(Long eventId, UpdateEventRequestDto adminRequest);

    Collection<EventShortDto> getAllPublic(EventRequestParamsDto params, String uri, String ip);

    EventFullDto getPublicById(Long id, String uri, String ip);

    EventFullDto create(Long userId, NewEventDto creationDto);

    EventFullDto update(Long userId, Long eventId, UpdateEventRequestDto userRequest);

    Collection<EventShortDto> getByUser(Long userId, Integer from, Integer size);

    EventFullDto get(Long userId, Long eventId);

    Collection<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResultDto updateRequestsStatus(Long userId, Long eventId,
                                                           EventRequestStatusUpdateRequestDto request);
}
