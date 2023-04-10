package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.service.StatsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public void create(@RequestBody EndpointHitDto endpointHitDto) {
        statsService.create(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> findStats(@RequestParam String start,
                                        @RequestParam String end,
                                        @RequestParam List<String> uris,
                                        @RequestParam(defaultValue = "false") Boolean unique) {
        return statsService.getStats(start, end, uris, unique);
    }
}
