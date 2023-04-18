package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.Collection;

public interface ParticipationRequestService {
    Collection<ParticipationRequestDto> getByUser(Long userId);

    ParticipationRequestDto create(Long userId, Long eventId);

    ParticipationRequestDto cancel(Long userId, Long requestId);
}
