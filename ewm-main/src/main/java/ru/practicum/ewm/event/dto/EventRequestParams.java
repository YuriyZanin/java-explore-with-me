package ru.practicum.ewm.event.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EventRequestParams {
    Long[] users;
    Long[] categories;
    String[] states;
    String rangeStart;
    String rangeEnd;
    Boolean paid;
    boolean onlyAvailable = false;
    String text;
    String sort;
    int from = 0;
    int size = 10;
}