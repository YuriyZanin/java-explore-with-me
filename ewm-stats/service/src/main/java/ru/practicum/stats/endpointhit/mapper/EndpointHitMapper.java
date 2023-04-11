package ru.practicum.stats.endpointhit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.endpointhit.model.EndpointHit;

@Mapper
public interface EndpointHitMapper {
    EndpointHitMapper MAPPER = Mappers.getMapper(EndpointHitMapper.class);

    EndpointHit toEndpointHit(EndpointHitDto dto);
}
