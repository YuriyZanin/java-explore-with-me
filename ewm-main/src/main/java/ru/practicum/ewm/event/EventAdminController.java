package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventRequestParams;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.service.EventService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public Collection<EventFullDto> findAll(EventRequestParams params) {
        log.info("Запрос событий администратором с параметрами={}", params);
        return eventService.getAll(params);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long eventId, @RequestBody @Valid UpdateEventRequest adminRequest) {
        log.info("Запрос на редактирование события администратором eventId={}, параметры={}", eventId, adminRequest);
        return eventService.updateByAdmin(eventId, adminRequest);
    }
}
