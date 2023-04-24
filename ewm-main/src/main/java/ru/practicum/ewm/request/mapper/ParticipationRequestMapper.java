package ru.practicum.ewm.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.utils.DateTimeUtils;

import java.util.List;

@Mapper
public interface ParticipationRequestMapper {
    ParticipationRequestMapper MAPPER = Mappers.getMapper(ParticipationRequestMapper.class);

    @Mapping(target = "event.id", source = "event")
    @Mapping(target = "requester.id", source = "requester")
    @Mapping(target = "created", source = "created", dateFormat = DateTimeUtils.DEFAULT_DATE_TIME_PATTERN)
    ParticipationRequest toParticipantRequest(ParticipationRequestDto requestDto);

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    @Mapping(target = "created", source = "created", dateFormat = DateTimeUtils.DEFAULT_DATE_TIME_PATTERN)
    ParticipationRequestDto toParticipantRequestDto(ParticipationRequest request);

    List<ParticipationRequestDto> toDtos(List<ParticipationRequest> requests);
}
