package ru.practicum.ewm.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;

import java.util.List;

@Mapper
public interface ParticipationRequestMapper {
    ParticipationRequestMapper MAPPER = Mappers.getMapper(ParticipationRequestMapper.class);

    @Mapping(target = "event.id", source = "event")
    @Mapping(target = "requester.id", source = "requester")
    ParticipationRequest toParticipantRequest(ParticipationRequestDto requestDto);

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestDto toParticipantRequestDto(ParticipationRequest request);

    List<ParticipationRequestDto> toDtos(List<ParticipationRequest> requests);
}
