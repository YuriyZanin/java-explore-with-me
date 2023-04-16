package ru.practicum.stats.endpointhit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.endpointhit.model.EndpointHit;
import ru.practicum.stats.endpointhit.model.ViewStats;
import ru.practicum.stats.utils.DateTimeUtils;

import java.util.List;

@Mapper
public interface EndpointHitMapper {
    EndpointHitMapper MAPPER = Mappers.getMapper(EndpointHitMapper.class);

    @Mapping(target = "timestamp", source = "timestamp", dateFormat = DateTimeUtils.DEFAULT_DATE_TIME_PATTERN)
    EndpointHit toEndpointHit(EndpointHitDto dto);

    List<ViewStatsDto> toViewStatsDtos(List<ViewStats> viewStats);
}
