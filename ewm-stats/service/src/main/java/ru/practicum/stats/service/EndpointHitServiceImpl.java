package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.mapper.EndpointHitMapper;
import ru.practicum.stats.repository.EndpointHitRepository;
import ru.practicum.stats.utils.DateTimeUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository endpointHitRepository;

    @Override
    public void create(EndpointHitDto endpointHitDto) {
        endpointHitRepository.save(EndpointHitMapper.MAPPER.toEndpointHit(endpointHitDto));
    }

    @Override
    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startDateTime = decodeDate(start);
        LocalDateTime endDateTime = decodeDate(end);
        if (unique) {
            return uris == null
                    ? endpointHitRepository.findByDateWithUniqueIp(startDateTime, endDateTime)
                    : endpointHitRepository.findByDateAndUrisWithUniqueIp(startDateTime, endDateTime, uris);
        } else {
            return uris == null
                    ? endpointHitRepository.findByDate(startDateTime, endDateTime)
                    : endpointHitRepository.findByDateAndUris(startDateTime, endDateTime, uris);
        }
    }

    private LocalDateTime decodeDate(String date) {
        String decodedDate = URLDecoder.decode(date, StandardCharsets.UTF_8);
        return LocalDateTime.parse(decodedDate, DateTimeUtils.DEFAULT_DATE_TIME_FORMATTER);
    }
}
