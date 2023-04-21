package ru.practicum.stats.endpointhit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.endpointhit.service.EndpointHitService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EndpointHitController {
    private final EndpointHitService endpointHitService;

    @PostMapping("/hit")
    public ResponseEntity<?> create(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("Сохранение информации о запросе пользователя");
        endpointHitService.create(endpointHitDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public Collection<ViewStatsDto> findStats(@RequestParam String start,
                                              @RequestParam String end,
                                              @RequestParam(required = false) List<String> uris,
                                              @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Получение статистики по посещениям с {} по {}, uris={}, unique={}", start, end, uris, unique);
        return endpointHitService.getStats(start, end, uris, unique);
    }
}
