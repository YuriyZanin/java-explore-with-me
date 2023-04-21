package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Slf4j
@Validated
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventFullDto> create(@PathVariable Long userId, @Validated @RequestBody NewEventDto creationDto) {
        log.info("Запрос на добавление события: userId={}, событие={}", userId, creationDto);
        return new ResponseEntity<>(eventService.create(userId, creationDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long userId, @PathVariable Long eventId,
                               @Validated @RequestBody UpdateEventRequest userRequest) {
        log.info("Запрос на редактирование события: userId={}, eventId={} параметры={}", userId, eventId, userRequest);
        return eventService.update(userId, eventId, userRequest);
    }

    @GetMapping
    public Collection<EventShortDto> findByUser(@PathVariable Long userId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Запрос событий добавленных пользователем: userId={}, from={}, size={}", userId, from, size);
        return eventService.getByUser(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto findById(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Зарпос события добавленного пользоавтелем: userId={}, eventId={}", userId, eventId);
        return eventService.get(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public Collection<ParticipationRequestDto> findRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получение информации о запросах на участие в событии: userId={}, eventId={}", userId, eventId);
        return eventService.getRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsStatus(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Изменение статуса заявок на участие в событии: userId={}, eventId={}, request={}",
                userId, eventId, request);
        return eventService.updateRequestsStatus(userId, eventId, request);
    }
}
