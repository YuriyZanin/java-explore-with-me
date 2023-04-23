package ru.practicum.ewm.event.dto;

import lombok.Value;

import java.util.List;

@Value
public class EventRequestStatusUpdateRequestDto {
    List<Long> requestIds;
    String status;
}
