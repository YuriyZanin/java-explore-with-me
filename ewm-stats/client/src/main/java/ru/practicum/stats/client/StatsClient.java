package ru.practicum.stats.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.utils.DateTimeUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Component
public class StatsClient extends BaseClient {

    public StatsClient(@Value("${stats-server.uri}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> saveRequest(String app, String path, String ip) {
        EndpointHitDto requestDto = EndpointHitDto.builder()
                .app(app)
                .uri(path)
                .ip(ip)
                .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        return post("/hit", requestDto);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String startStr = start.format(DateTimeUtils.DEFAULT_DATE_TIME_FORMATTER);
        String endStr = end.format(DateTimeUtils.DEFAULT_DATE_TIME_FORMATTER);
        Map<String, Object> parameters = Map.of(
                "start", URLEncoder.encode(startStr, StandardCharsets.UTF_8),
                "end", URLEncoder.encode(endStr, StandardCharsets.UTF_8),
                "uris", uris,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}
