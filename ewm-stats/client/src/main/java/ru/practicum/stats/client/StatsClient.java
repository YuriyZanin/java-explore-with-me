package ru.practicum.stats.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.practicum.stats.utils.DateTimeUtils.DEFAULT_DATE_TIME_FORMATTER;

public class StatsClient {
    private final WebClient client;

    public StatsClient(String serverUrl) {
        this.client = WebClient.create(serverUrl);
    }

    public void saveRequest(String app, String path, String ip) {
        String currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).format(DEFAULT_DATE_TIME_FORMATTER);
        EndpointHitDto hitDto = EndpointHitDto.builder().app(app).uri(path).ip(ip).timestamp(currentTime).build();
        client.post()
                .uri("/hit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(hitDto)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return client.get()
                .uri(uriBuilder -> uriBuilder.path("/stats")
                        .queryParam("start", start.format(DEFAULT_DATE_TIME_FORMATTER))
                        .queryParam("end", end.format(DEFAULT_DATE_TIME_FORMATTER))
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToFlux(ViewStatsDto.class)
                .collectList()
                .block();
    }
}