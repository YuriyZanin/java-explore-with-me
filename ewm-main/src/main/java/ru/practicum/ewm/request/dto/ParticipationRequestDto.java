package ru.practicum.ewm.request.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ParticipationRequestDto {
    Long id;
    Long event;
    Long requester;
    String created;
    String state;
}
