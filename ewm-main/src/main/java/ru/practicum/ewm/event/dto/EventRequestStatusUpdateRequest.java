package ru.practicum.ewm.event.dto;

import lombok.Value;

import java.util.List;

@Value
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    String status;
}
