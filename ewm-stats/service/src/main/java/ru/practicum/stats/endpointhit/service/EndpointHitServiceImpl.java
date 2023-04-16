package ru.practicum.stats.endpointhit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.endpointhit.repository.EndpointHitRepository;
import ru.practicum.stats.utils.DateTimeUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.stats.endpointhit.mapper.EndpointHitMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository endpointHitRepository;

    @Override
    public void create(EndpointHitDto endpointHitDto) {
        endpointHitRepository.save(MAPPER.toEndpointHit(endpointHitDto));
    }

    @Override
    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startDateTime = decodeDate(start);
        LocalDateTime endDateTime = decodeDate(end);
        if (unique) {
            return uris == null || uris.isEmpty()
                    ? MAPPER.toViewStatsDtos(endpointHitRepository.findByDateWithUniqueIp(startDateTime, endDateTime))
                    : MAPPER.toViewStatsDtos(endpointHitRepository.findByDateAndUrisWithUniqueIp(startDateTime, endDateTime, uris));
        } else {
            return uris == null || uris.isEmpty()
                    ? MAPPER.toViewStatsDtos(endpointHitRepository.findByDate(startDateTime, endDateTime))
                    : MAPPER.toViewStatsDtos(endpointHitRepository.findByDateAndUris(startDateTime, endDateTime, uris));
        }
    }

    private LocalDateTime decodeDate(String date) {
        String decodedDate = URLDecoder.decode(date, StandardCharsets.UTF_8);
        return LocalDateTime.parse(decodedDate, DateTimeUtils.DEFAULT_DATE_TIME_FORMATTER);
    }
}
