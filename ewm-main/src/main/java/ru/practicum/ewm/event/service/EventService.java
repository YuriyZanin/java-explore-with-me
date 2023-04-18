package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.Collection;

public interface EventService {
    Collection<EventFullDto> getAll(EventRequestParams params);

    EventFullDto updateByAdmin(Long eventId, UpdateEventRequest adminRequest);

    Collection<EventShortDto> getAllPublic(EventRequestParams params);

    EventFullDto getPublicById(Long id);

    EventFullDto create(Long userId, NewEventDto creationDto);

    EventFullDto update(Long userId, Long eventId, UpdateEventRequest userRequest);

    Collection<EventShortDto> getByUser(Long userId, Integer from, Integer size);

    EventFullDto get(Long userId, Long eventId);

    Collection<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestsStatus(Long userId, Long eventId,
                                                        EventRequestStatusUpdateRequest request);
}
