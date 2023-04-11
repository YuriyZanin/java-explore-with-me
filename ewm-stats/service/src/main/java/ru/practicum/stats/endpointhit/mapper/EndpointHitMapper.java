package ru.practicum.stats.endpointhit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.endpointhit.model.EndpointHit;
import ru.practicum.stats.utils.DateTimeUtils;

@Mapper
public interface EndpointHitMapper {
    EndpointHitMapper MAPPER = Mappers.getMapper(EndpointHitMapper.class);

    @Mapping(target = "timestamp", source = "timestamp", dateFormat = DateTimeUtils.DEFAULT_DATE_TIME_PATTERN)
    EndpointHit toEndpointHit(EndpointHitDto dto);
}
