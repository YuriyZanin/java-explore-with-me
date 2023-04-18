package ru.practicum.ewm.event.dto;

import lombok.Value;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

@Value
public class EventRequestStatusUpdateResult {
    List<ParticipationRequestDto> confirmedRequests;
    List<ParticipationRequestDto> rejectedRequests;
}
