package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventRequestParamsDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {
    private final EventService eventService;

    @GetMapping
    public Collection<EventShortDto> findAll(EventRequestParamsDto params, HttpServletRequest request) {
        log.info("Запрос на получение опубликованных событий с параметрами={}", params);
        return eventService.getAllPublic(params, request.getRequestURI(), request.getRemoteAddr());
    }

    @GetMapping("/{id}")
    public EventFullDto findById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Запрос на получение информации об опубликованном событии по id={}", id);
        return eventService.getPublicById(id, request.getRequestURI(), request.getRemoteAddr());
    }
}
