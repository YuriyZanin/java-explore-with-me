package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.service.EndpointHitService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EndpointHitController {
    private final EndpointHitService endpointHitService;

    @PostMapping("/hit")
    public void create(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Сохранение информации о запросе пользователя");
        endpointHitService.create(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> findStats(@RequestParam String start,
                                        @RequestParam String end,
                                        @RequestParam(required = false) List<String> uris,
                                        @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Получение статистики по посещениям с {} по {}, uris={}, unique={}", start, end, uris, unique);
        return endpointHitService.getStats(start, end, uris, unique);
    }
}
