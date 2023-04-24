package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.ParticipationRequestService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class ParticipationRequestController {
    private final ParticipationRequestService requestService;

    @GetMapping
    public Collection<ParticipationRequestDto> findAll(@PathVariable Long userId) {
        log.info("Получение информации о заявках текущего пользователя: userId={}", userId);
        return requestService.getByUser(userId);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> create(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Добавление запроса на участие в событии: userId={}, eventId={}", userId, eventId);
        return new ResponseEntity<>(requestService.create(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Отмена запроса на участие в событии: userId={}, requestId={}", userId, requestId);
        return requestService.cancel(userId, requestId);
    }
}
